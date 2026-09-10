package com.todo.controller;
import com.todo.model.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class TaskController {
  private final List<Task> tasks = new ArrayList<>();
  private final AtomicLong ids = new AtomicLong(1);

  @GetMapping("/")
  public String home(Model model){ model.addAttribute("tasks",tasks); return "index"; }

  @PostMapping("/add")
  public String add(@RequestParam String title,@RequestParam String description){
    if(title != null && !title.trim().isEmpty()) tasks.add(new Task(ids.getAndIncrement(),title.trim(),description,false));
    return "redirect:/";
  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable long id, Model model){
    Task task=find(id); if(task==null) return "redirect:/";
    model.addAttribute("task",task); return "edit";
  }

  @PostMapping("/update/{id}")
  public String update(@PathVariable long id,@RequestParam String title,@RequestParam String description,
                       @RequestParam(required=false) String completed){
    Task t=find(id); if(t!=null){t.setTitle(title);t.setDescription(description);t.setCompleted(completed!=null);}
    return "redirect:/";
  }

  @PostMapping("/delete/{id}")
  public String delete(@PathVariable long id){tasks.removeIf(t->t.getId()==id);return "redirect:/";}

  @PostMapping("/toggle/{id}")
  public String toggle(@PathVariable long id){Task t=find(id);if(t!=null)t.setCompleted(!t.isCompleted());return "redirect:/";}

  private Task find(long id){return tasks.stream().filter(t->t.getId()==id).findFirst().orElse(null);}
}
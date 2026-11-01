package com.CMEPPS.demo.application.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.CMEPPS.demo.core.domain.Todo;
import com.CMEPPS.demo.core.driver_ports.iTodoService;

@Controller
public class TodoController {

    @Autowired
    private iTodoService todoService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date - dd/MM/yyyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
    

    @RequestMapping(value = "/list-todos", method = RequestMethod.GET)
    public String showTodos(ModelMap model) {
        String name = getLoggedInUserName(model);
        model.put("todos", todoService.getTodosByUser(name));
        // model.put("todos", service.retrieveTodos(name));
        return "list-todos";
    }

    private String getLoggedInUserName(ModelMap model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        return principal.toString();
    }

    @RequestMapping(value = "/add-todo", method = RequestMethod.GET)
    public String showAddTodoPage(ModelMap model) {
        model.addAttribute("todo", new Todo());
        return "todo";
    }
    

    @RequestMapping(value = "/delete-todo", method = RequestMethod.GET)
    public String deleteTodo(@RequestParam long id) {
        todoService.deleteTodo(id);
        // service.deleteTodo(id);
        return "redirect:/list-todos";
    }

    @RequestMapping(value = "/update-todo", method = RequestMethod.GET)
    public String showUpdateTodoPage(@RequestParam long id, ModelMap model) {
        Todo todo = todoService.getTodoById(id).get();
        model.put("todo", todo);
        return "todo";
    }

    @RequestMapping(value = "/update-todo", method = RequestMethod.POST)
    public String updateTodo(ModelMap model, @Validated Todo todo, BindingResult result) {

        if (result.hasErrors()) {
            return "todo";
        }

        todo.setUserName(getLoggedInUserName(model));
        todoService.updateTodo(todo);
        return "redirect:/list-todos";
    }

    @PostMapping("/add-todo")
    public String addTodo(ModelMap model, @Validated Todo todo, BindingResult result) {

        if (result.hasErrors()) {
            return "todo";
        }

        todo.setUserName(getLoggedInUserName(model));
        todoService.saveTodo(todo);
        return "redirect:/list-todos";
    }
}
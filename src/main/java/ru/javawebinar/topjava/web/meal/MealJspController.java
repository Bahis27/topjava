package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MealJspController extends AbstractMealController{

    @GetMapping("/meals")
    public String users(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

}

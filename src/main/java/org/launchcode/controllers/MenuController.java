package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "My Menus");

        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model) {
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());
        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddMenuForm(@ModelAttribute @Valid Menu menu,
                                       Errors errors, @RequestParam String name, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            model.addAttribute("categories", menuDao.findAll());
            return "menu/add";}
        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }

    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int menuId) {
        Menu viewMenu = menuDao.findOne(menuId);
        model.addAttribute("menu", viewMenu);
        model.addAttribute("cheeses", viewMenu.getCheeses());
        model.addAttribute("title", viewMenu.getName());
        return "menu/view";
    }
    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int menuId) {
        Menu viewMenu = menuDao.findOne(menuId);
        Iterable<Cheese> viewCheese = cheeseDao.findAll();
        AddMenuItemForm addMenuItemForm = new AddMenuItemForm(viewMenu,viewCheese );
        model.addAttribute("addMenuItemForm", addMenuItemForm);
        model.addAttribute("title", "Add item to menu: "+viewMenu.getName());

        return "menu/add-item";
    }

    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String addItem(@ModelAttribute @Valid AddMenuItemForm addMenuItemForm,
                                     Errors errors, Model model){
        if (errors.hasErrors()) {
            String title = addMenuItemForm.getMenu().getName();
            model.addAttribute("addForm", addMenuItemForm);
            model.addAttribute("title", "Add item to menu: "+title);
            return "menu/add-item";
        }

        Menu menu = menuDao.findOne(addMenuItemForm.getMenuId());
        menu.addItem(cheeseDao.findOne(addMenuItemForm.getCheeseId()));
        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }



}

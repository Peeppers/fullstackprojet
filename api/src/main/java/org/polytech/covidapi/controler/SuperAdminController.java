package org.polytech.covidapi.controler;

import java.util.List;
import java.util.Optional;
import org.polytech.covidapi.model.SuperAdmin;
import org.polytech.covidapi.services.SuperAdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins="http://localhost:5432/")
@RequestMapping("/api")
public class SuperAdminController {
    
    @Autowired
    private SuperAdminServices userService;
    @GetMapping("/showsuperadmin")
    public String findUsers (Model model) {

        List<SuperAdmin> users = userService.findAll();
        String str = "";
        for (int i=0; i<users.size(); i++){
            SuperAdmin currentuser = users.get(i);
            str = str + "\n" + currentuser;
        }

        return str;
    }

    @GetMapping("/showsuperadmin/{id}")
    public Optional<SuperAdmin> getOneacteur(@PathVariable int id){
            Optional<SuperAdmin> user = userService.findById(id);
            return user;
    }

    @PostMapping(path = "/addsuperadmin")
    public SuperAdmin save(@RequestBody SuperAdmin newuser) {
        return userService.save(newuser);
    }

    @DeleteMapping("/deletesuperadmin/{id}")
    public void delete(@PathVariable int id){
        userService.delete(id);
    }


}
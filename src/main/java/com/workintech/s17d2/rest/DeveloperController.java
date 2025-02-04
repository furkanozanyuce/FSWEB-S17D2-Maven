package com.workintech.s17d2.rest;

import jakarta.annotation.PostConstruct;
import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.model.JuniorDeveloper;
import com.workintech.s17d2.model.MidDeveloper;
import com.workintech.s17d2.model.SeniorDeveloper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.workintech.s17d2.tax.Taxable;

import java.util.*;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    public Map<Integer, Developer> developers = new HashMap<>();
    private Taxable taxable;

    @PostConstruct
    public void init() {
        Developer developer = new JuniorDeveloper(65, "Ozan", 1000 - (1000 * taxable.getSimpleTaxRate() / 100));
        developers.put(developer.getId(), developer);
    }

    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable Integer id) {
        return developers.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Developer addDeveloper(@RequestBody Developer developer) {
        double finalSalary;
        switch (developer.getExperience()) {
            case JUNIOR:
                finalSalary = developer.getSalary() - (developer.getSalary() * taxable.getSimpleTaxRate() / 100);
                developer = new JuniorDeveloper(developer.getId(), developer.getName(), finalSalary);
                break;
            case MID:
                finalSalary = developer.getSalary() - (developer.getSalary() * taxable.getMiddleTaxRate() / 100);
                developer = new MidDeveloper(developer.getId(), developer.getName(), finalSalary);
                break;
            case SENIOR:
                finalSalary = developer.getSalary() - (developer.getSalary() * taxable.getUpperTaxRate() / 100);
                developer = new SeniorDeveloper(developer.getId(), developer.getName(), finalSalary);
                break;
            default:
                throw new IllegalArgumentException("Invalid experience level");
        }
        developers.put(developer.getId(), developer);
        return developer;
    }

    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable Integer id, @RequestBody Developer developer) {
        if (!developers.containsKey(id)) {
            throw new NoSuchElementException("Developer not found with id " + id);
        }
        double finalSalary;
        switch (developer.getExperience()) {
            case JUNIOR:
                finalSalary = developer.getSalary() - (developer.getSalary() * taxable.getSimpleTaxRate() / 100);
                developer = new JuniorDeveloper(id, developer.getName(), finalSalary);
                break;
            case MID:
                finalSalary = developer.getSalary() - (developer.getSalary() * taxable.getMiddleTaxRate() / 100);
                developer = new MidDeveloper(id, developer.getName(), finalSalary);
                break;
            case SENIOR:
                finalSalary = developer.getSalary() - (developer.getSalary() * taxable.getUpperTaxRate() / 100);
                developer = new SeniorDeveloper(id, developer.getName(), finalSalary);
                break;
            default:
                throw new IllegalArgumentException("Invalid experience level");
        }
        developers.put(id, developer);
        return developer;
    }

    @DeleteMapping("/{id}")
    public String deleteDeveloper(@PathVariable Integer id) {
        if (developers.remove(id) != null) {
            return "Developer id: " + id + " silindi.";
        } else {
            return "Developer id: " + id + " bulunamadı.";
        }
    }
}








































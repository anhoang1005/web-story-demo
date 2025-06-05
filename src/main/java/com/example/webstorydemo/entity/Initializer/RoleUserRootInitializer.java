package com.example.webstorydemo.entity.Initializer;

import com.example.webstorydemo.entity.Roles;
import com.example.webstorydemo.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@AllArgsConstructor
@Component
@Slf4j
public class RoleUserRootInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    private void createRole(){
        try {
            List<Roles.BaseRole> listRole = List.of(Roles.BaseRole.ADMIN,
                    Roles.BaseRole.USER);
            List<Roles> listEntity = new ArrayList<>();
            for(Roles.BaseRole role : listRole){
                if(!roleRepository.existsRolesByRoleName(role)){
                    Roles roles = new Roles();
                    roles.setRoleName(role);
                    listEntity.add(roles);
                }
            }
            if(!listEntity.isEmpty()){
                roleRepository.saveAll(listEntity);
                log.info("Create all root roles success!");
            } else {
                log.info("All root existed!");
            }
        } catch (Exception e) {
            log.error("Create root roles error! Error: {}", e.getMessage());
        }
    }

    @Override
    public void run(String... args) {
        createRole();
    }
}

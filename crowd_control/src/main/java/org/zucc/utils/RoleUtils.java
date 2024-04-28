package org.zucc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoleUtils {

    public static List<String> getRole(String systemId, String role) {
        ArrayList<String> roles = new ArrayList<>();
        roles.add(systemId + "-指挥中心");
        roles.add(systemId + "-公安");
        roles.add(systemId + "-交警");
        roles.add(systemId + "-志愿者");
        roles.add(systemId + "-城管");
        roles.add(systemId + "-市民");
        roles.add(systemId + "-公交地铁");
        return roles.stream().filter(e -> !(e.equals(systemId + "-" + role))).collect(Collectors.toList());
    }
}

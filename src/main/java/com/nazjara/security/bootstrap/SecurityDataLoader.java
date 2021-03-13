package com.nazjara.security.bootstrap;

import com.nazjara.security.domain.Authority;
import com.nazjara.security.domain.Role;
import com.nazjara.security.domain.User;
import com.nazjara.security.repository.AuthorityRepository;
import com.nazjara.security.repository.RoleRepository;
import com.nazjara.security.repository.UserRepository;
import com.nazjara.service.domain.BeerOrder;
import com.nazjara.service.domain.BeerOrderLine;
import com.nazjara.service.domain.Customer;
import com.nazjara.service.domain.OrderStatusEnum;
import com.nazjara.service.repository.BeerOrderRepository;
import com.nazjara.service.repository.BeerRepository;
import com.nazjara.service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

import static com.nazjara.service.bootstrap.DefaultBreweryLoader.BEER_1_UPC;

@RequiredArgsConstructor
@Component
@Slf4j
@Order(2)
public class SecurityDataLoader implements CommandLineRunner {

    public static final String ST_PETE_DISTRIBUTING = "St Pete Distributing";
    public static final String DUNEDIN_DISTRIBUTING = "Dunedin Distributing";
    public static final String KEY_WEST_DISTRIBUTORS = "Key West Distributors";
    public static final String STPETE_USER = "stpete";
    public static final String DUNEDIN_USER = "dunedin";
    public static final String KEYWEST_USER = "keywest";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) {
        if (authorityRepository.count() == 0) {
            loadData();
        }
    }

    private void loadData() {
        var authority1 = authorityRepository.save(Authority.builder()
                .permission("beer.create")
                .build());
        var authority2 = authorityRepository.save(Authority.builder()
                .permission("beer.delete")
                .build());
        var authority3 = authorityRepository.save(Authority.builder()
                .permission("beer.read")
                .build());
        var authority4 = authorityRepository.save(Authority.builder()
                .permission("beer.update")
                .build());
        var authority5 = authorityRepository.save(Authority.builder()
                .permission("customer.create")
                .build());
        var authority6 = authorityRepository.save(Authority.builder()
                .permission("customer.delete")
                .build());
        var authority7 = authorityRepository.save(Authority.builder()
                .permission("customer.read")
                .build());
        var authority8 = authorityRepository.save(Authority.builder()
                .permission("customer.update")
                .build());
        var authority9 = authorityRepository.save(Authority.builder()
                .permission("brewery.create")
                .build());
        var authority10 = authorityRepository.save(Authority.builder()
                .permission("brewery.delete")
                .build());
        var authority11 = authorityRepository.save(Authority.builder()
                .permission("brewery.read")
                .build());
        var authority12 = authorityRepository.save(Authority.builder()
                .permission("brewery.update")
                .build());
        var authority13 = authorityRepository.save(Authority.builder()
                .permission("order.create")
                .build());
        var authority14 = authorityRepository.save(Authority.builder()
                .permission("order.delete")
                .build());
        var authority15 = authorityRepository.save(Authority.builder()
                .permission("order.read")
                .build());
        var authority16 = authorityRepository.save(Authority.builder()
                .permission("order.update")
                .build());
        var authority17 = authorityRepository.save(Authority.builder()
                .permission("customer.order.create")
                .build());
        var authority18 = authorityRepository.save(Authority.builder()
                .permission("customer.order.delete")
                .build());
        var authority19 = authorityRepository.save(Authority.builder()
                .permission("customer.order.read")
                .build());
        var authority20 = authorityRepository.save(Authority.builder()
                .permission("customer.order.update")
                .build());
        var authority21 = authorityRepository.save(Authority.builder()
                .permission("order.pickup")
                .build());
        var authority22 = authorityRepository.save(Authority.builder()
                .permission("customer.order.pickup")
                .build());

        var role1 = roleRepository.save(Role.builder()
                .name("ADMIN")
                .build());
        var role2 = roleRepository.save(Role.builder()
                .name("USER")
                .build());
        var role3 = roleRepository.save(Role.builder()
                .name("CUSTOMER")
                .build());
        
        role1.setAuthorities(Set.of(authority1, authority2, authority3, authority4, authority5, authority6, authority7,
                        authority8, authority9, authority10, authority11, authority12, authority13, authority14, authority15,
                authority16, authority21));

        role2.setAuthorities(Set.of(authority3));

        role3.setAuthorities(Set.of(authority3, authority7, authority11, authority17, authority18, authority19,
                authority20, authority22));

        roleRepository.saveAll(Set.of(role1, role2, role3));

        var user1 = userRepository.save(User.builder()
                .username("user1")
                .password(passwordEncoder.encode("password1"))
                .build());

        var user2 = userRepository.save(User.builder()
                .username("user2")
                .password(passwordEncoder.encode("password2"))
                .build());

        var user3 = userRepository.save(User.builder()
                .username("user3")
                .password(passwordEncoder.encode("password3"))
                .build());

        user1.setRoles(Set.of(role1));
        user2.setRoles(Set.of(role2));
        user3.setRoles(Set.of(role3));

        userRepository.saveAll(Set.of(user1, user2, user3));

        Customer stPeteCustomer = customerRepository.save(Customer.builder()
                .customerName(ST_PETE_DISTRIBUTING)
                .apiKey(UUID.randomUUID())
                .build());

        Customer dunedinCustomer = customerRepository.save(Customer.builder()
                .customerName(DUNEDIN_DISTRIBUTING)
                .apiKey(UUID.randomUUID())
                .build());

        Customer keyWestCustomer = customerRepository.save(Customer.builder()
                .customerName(KEY_WEST_DISTRIBUTORS)
                .apiKey(UUID.randomUUID())
                .build());

        User stPeteUser = userRepository.save(User.builder().username("stpete")
                .password(passwordEncoder.encode("password"))
                .customer(stPeteCustomer)
                .role(role3).build());

        User dunedinUser = userRepository.save(User.builder().username("dunedin")
                .password(passwordEncoder.encode("password"))
                .customer(dunedinCustomer)
                .role(role3).build());

        User keywest = userRepository.save(User.builder().username("keywest")
                .password(passwordEncoder.encode("password"))
                .customer(keyWestCustomer)
                .role(role3).build());

        createOrder(stPeteCustomer);
        createOrder(dunedinCustomer);
        createOrder(keyWestCustomer);

        log.debug("Orders Loaded: " + beerOrderRepository.count());
    }

    private BeerOrder createOrder(Customer customer) {
        return  beerOrderRepository.save(BeerOrder.builder()
                .customer(customer)
                .orderStatus(OrderStatusEnum.NEW)
                .beerOrderLines(Set.of(BeerOrderLine.builder()
                        .beer(beerRepository.findByUpc(BEER_1_UPC))
                        .orderQuantity(2)
                        .build()))
                .build());
    }
}

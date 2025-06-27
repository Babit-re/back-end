package com.babit.demo.domain.user.repository;

import com.babit.demo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; //DB에서 값을 못 찾았을 때 null 대신 Optional.empty()를 반환

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}

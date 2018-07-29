package pl.damianlegutko.fprecrutation.user;

import org.springframework.data.jpa.repository.JpaRepository;

interface RoleRepository extends JpaRepository<Role, Long> {
}
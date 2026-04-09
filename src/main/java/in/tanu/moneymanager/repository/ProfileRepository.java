package in.tanu.moneymanager.repository;

import in.tanu.moneymanager.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity,Long> {
    // This interface will automatically provide crud operations for profileentity
    // you can add custom query methods here if needed
    // select * from tbl_profiles where email=?
    Optional<ProfileEntity> findByEmail(String email);

    // select * from tbl_profiles where activation_token=?
    Optional<ProfileEntity> findByActivationToken(String activationToken);

}

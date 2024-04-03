package Url.Shortner.Release.Repository;

import Url.Shortner.Release.Model.ShortnerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortnerRepository extends JpaRepository<ShortnerModel, Long> {

    public ShortnerModel findByHash(String hash);

    public ShortnerModel findByUrl(String url);

    public Boolean existsByUrl(String url);

}

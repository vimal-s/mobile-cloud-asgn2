package org.magnum.mobilecloud.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findByName(String name);

    List<Video> findByDurationLessThan(long duration);
}

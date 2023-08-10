package com.weffy.mattermost.repository;

import com.weffy.mattermost.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaChannelRepository extends JpaRepository<Channel, Long> {
    Optional<Channel> findByIdentification(String identification);
}

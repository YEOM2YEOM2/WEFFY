package com.weffy.mattermost.repository;

import com.weffy.mattermost.entity.Channel;
import com.weffy.mattermost.entity.Team;
import com.weffy.mattermost.entity.WeffyUserChannel;
import com.weffy.user.entity.WeffyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserChannelRepository extends JpaRepository<WeffyUserChannel, Long> {
    Optional<WeffyUserChannel> findByChannelAndWeffyUser(Channel channel, WeffyUser weffyUser);

    List<WeffyUserChannel> findByWeffyUserAndChannel_Team(WeffyUser weffyUser, Team team);
}

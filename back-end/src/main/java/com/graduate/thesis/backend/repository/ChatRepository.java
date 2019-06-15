package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Huy Pham
 */

@Repository
public interface ChatRepository extends MongoRepository<Conversation, String> {

    @Query("{ '_id': ?0 }")
    Conversation findConversationById(String id);

    List<Conversation> findByMembersContains(String userId);

    Conversation findByMembersEquals(List<String> members);

}

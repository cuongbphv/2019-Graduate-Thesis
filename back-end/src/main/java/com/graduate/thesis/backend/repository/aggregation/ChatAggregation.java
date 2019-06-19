package com.graduate.thesis.backend.repository.aggregation;

import com.graduate.thesis.backend.entity.Category;
import com.graduate.thesis.backend.entity.Conversation;
import com.graduate.thesis.backend.model.response.ConversationResponse;
import com.graduate.thesis.backend.model.websocket.ChatMessage;
import com.graduate.thesis.backend.repository.aggregation.operation.CustomProjectAggregationOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @author Huy Pham
 */
@Repository
public class ChatAggregation {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ChatAggregation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void addMessage(ChatMessage chatMessage, String conversationId){
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(conversationId)),
                new Update().push("messages",chatMessage), Conversation.class);
    }

    public void updateMessageContent(ChatMessage chatMessage, String conversationId){
        mongoTemplate.findAndModify(
                Query
                        .query(
                                Criteria
                                        .where("_id").is(conversationId)
                                        .and("messages.messageId").is(chatMessage.getMessageId())),
                new Update().set("messages.$.content",chatMessage.getContent()), Conversation.class);
    }

    public void updateMessageStatus(ChatMessage chatMessage, String conversationId){
        mongoTemplate.findAndModify(
                Query
                        .query(
                                Criteria
                                        .where("_id").is(conversationId)
                                        .and("messages.messageId").is(chatMessage.getMessageId())),
                new Update().set("messages.$.status",chatMessage.getStatus()), Conversation.class);
    }

    public List<ConversationResponse> getAllConversationData(String userId, int size){

        String matchQuery =
                "   { $match:   { members: { $all: [ \"" + userId+ "\"] },  messages: { $exists: true }  } }";

        String lookupQuery =
                "{ \"$lookup\": {\n" +
                        "       \"from\": \"user_profile\",\n" +
                        "       \"localField\": \"members\",\n" +
                        "       \"foreignField\": \"userId\",\n" +
                        "       \"as\": \"memberObjects\"\n" +
                        "    }}";

        String groupQuery =
                "    { \"$group\": {\n" +
                        "        \"_id\": \"$_id\",\n" +
                        "        \"createdDate\": { \"$first\": \"$createdDate\" },\n" +
                        "        \"lastMessage\": { \"$first\": { \"$arrayElemAt\": [ \"$messages\", -1 ] } },\n" +
                        "        \"members\": { \"$push\": \"$memberObjects\" }\n" +
                        "    }}";

        String unwindQuery =
                "{ \"$unwind\": \"$memberObjects\" }";

        String projectQuery =
                "   {\n" +
                        "     $project:\n" +
                        "      {\n" +
                        "         _id: 1,\n" +
                        "         members: 1,\n" +
                        "         createdDate: 1,\n" +
                        "         lastMessage: { $arrayElemAt: [ \"$messages\", -1 ] }\n" +
                        "      }\n" +
                        "   }";

        String sortQuery =
                "{ $sort: { 'lastMessage.createdDate': -1 } }";

        return mongoTemplate.aggregate(Aggregation.newAggregation(
                new CustomProjectAggregationOperation(matchQuery),
                unwind("members"),
                new CustomProjectAggregationOperation(lookupQuery),
                new CustomProjectAggregationOperation(unwindQuery),
                new CustomProjectAggregationOperation(groupQuery),
                new CustomProjectAggregationOperation(sortQuery),
                limit(size)),
                Conversation.class, ConversationResponse.class).getMappedResults();
    }


    public ConversationResponse getConversationData(String conversationId){

        String lookupQuery =
                "{ \"$lookup\": {\n" +
                        "       \"from\": \"user_profile\",\n" +
                        "       \"localField\": \"members\",\n" +
                        "       \"foreignField\": \"userId\",\n" +
                        "       \"as\": \"memberObjects\"\n" +
                        "    }}";

        String groupQuery =
                "    { \"$group\": {\n" +
                        "        \"_id\": \"$_id\",\n" +
                        "        \"createdDate\": { \"$first\": \"$createdDate\" },\n" +
                        "        \"lastMessage\": { \"$first\": { \"$arrayElemAt\": [ \"$messages\", -1 ] } },\n" +
                        "        \"members\": { \"$push\": \"$memberObjects\" }\n" +
                        "    }}";

        String unwindQuery =
                "{ \"$unwind\": \"$memberObjects\" }";


        return mongoTemplate.aggregate(Aggregation.newAggregation(
                match(Criteria.where("_id").is(conversationId)),
                unwind("members"),
                new CustomProjectAggregationOperation(lookupQuery),
                new CustomProjectAggregationOperation(unwindQuery),
                new CustomProjectAggregationOperation(groupQuery)),
                Conversation.class, ConversationResponse.class).getUniqueMappedResult();
    }

    public List<ChatMessage> getMessageHistoryByConversationIdAndSize(String conversationId, int size){

        String projectQuery =
                "{ $project: { _id: 0, messages: 1 } }";

        String replaceRootQuery =
                "{ $replaceRoot: { newRoot: \"$messages\" } }";

        String sortQuery =
                "{ $sort: { createdDate: -1 } }";

        return mongoTemplate.aggregate(Aggregation.newAggregation(
                match(Criteria.where("_id").is(conversationId)),
                new CustomProjectAggregationOperation(projectQuery),
                unwind("messages"),
                new CustomProjectAggregationOperation(replaceRootQuery),
                new CustomProjectAggregationOperation(sortQuery),
                limit(size)),
                Conversation.class, ChatMessage.class).getMappedResults();

    }

}

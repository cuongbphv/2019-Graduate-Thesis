<template>
  <div class="chat">
    <div class="messaging">
      <div class="inbox_msg">
        <div class="inbox_people">
          <div class="headind_srch">
            <div class="recent_heading">
              <h4>Recent</h4>
            </div>
            <div class="srch_bar">
              <div class="stylish-input-group">
                <el-input class="search-bar" placeholder="Search" />
              </div>
            </div>
          </div>
          <div class="inbox_chat">
            <div
              v-for="conversation in conversations"
              :key="conversation.id"
              class="chat_list"
              :class="{ active_chat: conversation.id === selectedConversationId }"
              @click="selectConversation(conversation)"
            >
              <div v-if="conversation.lastMessage" class="chat_people">
                <template v-for="user in conversation.members">
                  <div v-if="user.userId !== userId" :key="user.id" class="chat_img"> <img :src="user.avatarUrl" alt="sunil"> </div>
                  <div v-if="user.userId !== userId" :key="user.userId" :class="{ unread: conversation.lastMessage.status !== 'SEEN' }" class="chat_ib">
                    <h5> {{ user.firstName + ' ' + user.lastName }}
                      <span class="chat_date">{{ formatDate(conversation.lastMessage.createdDate) }}</span>
                    </h5>
                    <p> {{ conversation.lastMessage.content }}</p>
                  </div>
                </template>
              </div>
            </div>
            <infinite-loading v-if="conversations.length > 0" force-use-infinite-wrapper=".inbox_chat" @infinite="infiniteConversationHandler" />
          </div>
        </div>
        <div class="mesgs">
          <div id="msg-container" class="msg_history">
            <!--<div class="mention-post">-->
            <!--<div class="post-image" style="display: inline-block"><img src="https://ptetutorials.com/images/user-profile.png" width="50" height="50" alt="sunil"></div>-->
            <!--<div class="post-title" style="display: inline-block"><span>Bán chiếc xe cùi bắp</span></div>-->
            <!--</div>-->
            <infinite-loading v-if="selectedConversationId" direction="top" force-use-infinite-wrapper=".msg_history" @infinite="infiniteMessageHandler" />
            <template v-for="msg in messageHistory">
              <div v-if="msg.senderId !== userId" :key="msg.id" class="incoming_msg">
                <template v-for="user in currentConversation.members">
                  <div v-if="user.userId !== userId" :key="user.id" class="incoming_msg_img"> <img :src="user.avatarUrl" alt="sunil"> </div>
                </template>
                <div class="received_msg">
                  <div class="received_withd_msg">
                    <p>{{ msg.content }}</p>
                    <span class="time_date"> {{ formatDate(msg.createdDate) }}</span></div>
                </div>
              </div>
              <div v-else :key="msg.id" class="outgoing_msg">
                <div class="sent_msg">
                  <p>{{ msg.content }}</p>
                  <span class="time_date"> {{ formatDate(msg.createdDate) }}</span> </div>
              </div>
            </template>
          </div>
          <div class="type_msg">
            <div class="input_msg_write">
              <el-input v-model="messageTemplate" type="text" class="write_msg" placeholder="Type a message" />
              <el-button class="msg_send_btn" @click="sendMessage"><i class="el-icon-position" /></el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { Stomp } from '@stomp/stompjs'
import * as SockJS from 'sockjs-client'
import { mapActions, mapGetters } from 'vuex'
import date from '@/utils/date'
export default {
  name: 'Chat',
  data() {
    return {
      socketUrl: 'http://localhost:8080/socket',
      conversations: [],
      conversationSize: 10,
      selectedConversationId: '',
      currentConversation: {},
      messageTemplate: '',
      messageSize: 10,
      loadMoreSize: 10
    }
  },
  computed: {
    ...mapGetters('profile', ['userId']),
    ...mapGetters('chat', ['messageHistory'])
  },
  watch: {
    selectedConversationId: function(newVal) {
      this.messageTemplate = ''
      this.messageSize = 10
      this.getConversation(newVal)
      this.getMessages(newVal, this.scrollChatToBottom())
    }
  },
  created() {
    this.getAllConversation()
    this.subscribeMessage()
  },
  mounted() {
  },
  methods: {
    ...mapActions('chat', ['getAllConversationByUserId', 'getConversationById', 'addMessage', 'getMessageHistory']),
    getAllConversation() {
      this.getAllConversationByUserId({ userId: this.userId, size: this.conversationSize }).then(data => {
        this.conversations = Object.assign([], data)
        if (data.length > 0) {
          this.selectedConversationId = data[0].id
        }
      })
    },
    getConversation(id) {
      this.getConversationById(id).then(data => {
        this.currentConversation = Object.assign({}, data)
      })
    },
    getMessages(conversationId, callback) {
      this.getMessageHistory({ id: conversationId, size: this.messageSize })
        .then(() => {
          !!callback && callback()
        })
    },
    sendMessage() {
      const params = {
        conversationId: this.selectedConversationId,
        senderId: this.userId,
        content: this.messageTemplate
      }
      const that = this
      this.addMessage(params).then(() => {
        that.messageTemplate = ''
      })
    },
    selectConversation(conversation) {
      this.selectedConversationId = conversation.id
    },
    loadMore() {
      if (this.selectedConversationId) {
        this.messageSize = this.loadMoreSize + this.messageHistory.length
        this.getMessages(this.selectedConversationId)
      }
    },
    infiniteMessageHandler($state) {
      this.messageSize = this.loadMoreSize + this.messageHistory.length
      this.getMessageHistory({ id: this.selectedConversationId, size: this.messageSize })
        .then((data) => {
          if (data.length + this.loadMoreSize !== this.messageSize) {
            $state.loaded()
          } else {
            $state.complete()
          }
        })
    },
    infiniteConversationHandler($state) {
      this.conversationSize = this.loadMoreSize + this.conversations.length
      this.getAllConversationByUserId({ userId: this.userId, size: this.conversationSize })
        .then((data) => {
          this.conversations = Object.assign([], data)
          if (data.length + this.loadMoreSize !== this.conversationSize) {
            $state.loaded()
          } else {
            $state.complete()
          }
        })
    },
    subscribeMessage() {
      const ws = new SockJS(this.socketUrl)
      this.stompClient = Stomp.over(ws)
      const that = this
      this.stompClient.connect({}, function() {
        that.stompClient.subscribe('/user/5cae4c886bf46d0b5453a66f/queue/chat', (message) => {
          if (message.body) {
            const body = JSON.parse(message.body)
            switch (body.type) {
              case 'CONVERSATION':
                break
              case 'MESSAGE':
                if (body.conversationId === that.selectedConversationId) {
                  that.messageHistory.push(body.data)
                }
                that.handleUpdateConversationWithNewMessage(body.conversationId, body.data)
                setTimeout(that.scrollChatToBottom, 500)
                break
              default:
              // code block
            }
            // if (that.currentConversation.messages) {
            //   that.currentConversation.messages.push(body)
            // } else {
            //   that.currentConversation.messages = []
            //   that.currentConversation.messages.push(body)
            // }
          }
        })
      })
    },
    handleUpdateConversationWithNewMessage(conversationId, message) {
      const index = this.conversations.findIndex(function(element) {
        return element.id === conversationId
      })
      if (index !== -1) {
        this.conversations[index].lastMessage = message
        this.handleSortConversation()
      } else {
        this.getConversationById(conversationId).then(data => {
          if (data) {
            this.conversations.unshift(data)
          }
        })
      }
    },
    handleSortConversation() {
      function compare(a, b) { // sort messages by date
        if (a.lastMessage.createdDate > b.lastMessage.createdDate) {
          return -1
        }
        if (a.lastMessage.createdDate < b.lastMessage.createdDate) {
          return 1
        }
        return 0
      }
      this.conversations = Object.assign([], this.conversations.sort(compare))
    },
    formatDate(rawDate) {
      return date.formatDate(rawDate)
    },
    scrollChatToBottom() {
      const container = this.$el.querySelector('#msg-container')
      container.scrollTop = container.scrollHeight
    }
  }
}
</script>
<style lang="scss" scoped>
  .chat{max-width:1170px; margin:auto;}
  img{ max-width:100%;}
  .inbox_people {
    background: #f8f8f8 none repeat scroll 0 0;
    float: left;
    overflow: hidden;
    width: 40%; border-right:1px solid #c4c4c4;
  }
  .unread {
    font-weight: bold;
  }
  .inbox_msg {
    border: 1px solid #c4c4c4;
    clear: both;
    overflow: hidden;
  }
  .top_spac{ margin: 20px 0 0;}
  .recent_heading {float: left; width:40%;}
  .srch_bar {
    display: inline-block;
    text-align: right;
    width: 60%;
    /*padding:*/
  }
  .headind_srch{ padding:10px 29px 10px 20px; overflow:hidden; border-bottom:1px solid #c4c4c4;}

  .recent_heading h4 {
    color: #05728f;
    font-size: 21px;
    margin: auto;
  }
  .srch_bar input{ border:1px solid #cdcdcd; border-width:0 0 1px 0; width:80%; padding:2px 0 4px 6px; background:none;}
  .srch_bar .input-group-addon button {
    background: rgba(0, 0, 0, 0) none repeat scroll 0 0;
    border: medium none;
    padding: 0;
    color: #707070;
    font-size: 18px;
  }
  .srch_bar .input-group-addon { margin: 0 0 0 -27px;}

  .chat_ib h5{ font-size:15px; color:#464646; margin:0 0 8px 0;}
  .chat_ib h5 span{ font-size:13px; float:right;}
  .chat_ib p{ font-size:14px; color:#989898; margin:auto}
  .chat_img {
    float: left;
    width: 11%;
    img{
      border-radius: 50%;
    }
  }
  .chat_ib {
    float: left;
    padding: 0 0 0 15px;
    width: 88%;
  }

  .chat_people{ overflow:hidden; clear:both;}
  .chat_list {
    border-bottom: 1px solid #c4c4c4;
    margin: 0;
    padding: 18px 16px 10px;
  }
  .inbox_chat { height: 550px; overflow-y: scroll;}

  .active_chat{ background:#ebebeb;}

  .incoming_msg_img {
    display: inline-block;
    width: 6%;
    img{
      border-radius: 50%;
    }
  }
  .received_msg {
    display: inline-block;
    padding: 0 0 0 10px;
    vertical-align: top;
    width: 92%;
  }
  .received_withd_msg p {
    background: #ebebeb none repeat scroll 0 0;
    border-radius: 3px;
    color: #646464;
    font-size: 14px;
    margin: 0;
    padding: 5px 10px 5px 12px;
    width: 100%;
  }
  .time_date {
    color: #747474;
    display: block;
    font-size: 12px;
    margin: 8px 0 0;
  }
  .received_withd_msg { width: 57%;}
  .mesgs {
    float: left;
    padding: 30px 15px 0 25px;
    width: 60%;
  }

  .sent_msg p {
    background: #05728f none repeat scroll 0 0;
    border-radius: 3px;
    font-size: 14px;
    margin: 0; color:#fff;
    padding: 5px 10px 5px 12px;
    width:100%;
  }
  .outgoing_msg{ overflow:hidden; margin:26px 0 26px;}
  .sent_msg {
    float: right;
    width: 46%;
  }
  .input_msg_write input {
    background: rgba(0, 0, 0, 0) none repeat scroll 0 0;
    border: medium none;
    color: #4c4c4c;
    font-size: 15px;
    min-height: 48px;
    width: 100%;
  }

  .type_msg {border-top: 1px solid #c4c4c4;position: relative;}
  .msg_send_btn {
    background: #05728f none repeat scroll 0 0;
    border: medium none;
    border-radius: 50%;
    color: #fff;
    cursor: pointer;
    font-size: 17px;
    height: 33px;
    position: absolute;
    right: 0;
    top: 11px;
    width: 33px;
  }
  .messaging { padding: 0 0 50px 0;}
  .msg_history {
    height: 516px;
    overflow-y: auto;
  }
</style>

<template>
<div @scroll="getArticle" class="router-wrapper">
    <div class="router-wrapper2">
        <div v-for="(item, index) in this.boardList" :key="index">
          <div class="board" v-if="item.delAt === 'N'">
          <div class="null-content" v-if="item.isNull">{{item.content}}</div>
          <div v-else>
            <div class="name-div">
                <div>
                    <div>{{item.member.memNick}}</div>
                    <div>{{item.boardDate}}</div>
                </div>
                <!-- 이 부분에다가 v-if로 토큰값 비교해서 작성자일 경우 수정,삭제 버튼.. 아닐경우 신고 버튼-->
                <div class="icon-container" v-if="item.수정했니 === false">
                    <div class="icon-div">
                        <i @click="this.changeBoardIsModify(item); 
                                    this.changeIsUpdate(item);"
                                    class="fas fa-edit"
                            v-if="this.updateCheck == false"></i>
                    </div>
                    <div class="icon-div">
                        <i @click="confirmDelete(item)" class="far fa-trash-alt"></i>
                    </div>
                    <!-- 밑의 div에다가 update axios를 하는 메소드 이름을 @click에다가 추가-->
                </div>
                <div class="report-div">
                    <span @click="report(item); changeIsReportClick()" v-if="isReportClick === false">
                        <img class="no-report" src="@/assets/noneReport.png">
                    </span>
                    <span @click="cancelReport(item); changeIsReportClick()" v-if="isReportClick === true">
                        <img class="report" src="@/assets/report.png">
                    </span>
                </div>
                <div id="finish-div" v-if="item.수정했니 === true"
                                    @click="exportFinish(item); increasingIsExportUpdate()">Finish
                </div>
            </div>
            <div class="content-div no-read-only" v-if="item.isModify == true">
                {{ item.boardCn }}
            </div>

            <div class="content-div read-only" v-if="item.isModify == false">
                <editor :originContent="item" :isExport="isExport" @exportContent="getContent" class="content-div"/>
            </div>
            <div id="btn-div">
                <div @click="like(item); setLikeFlag()" v-if="!likeToggle">
                    <i class="far fa-thumbs-up"></i>
                    <span>{{ item.totalLikes }} 개</span>
                </div>
                <div class="clickedThumbs-up" @click="cancelLike(item); setLikeFlag()" v-else>
                    <i class="far fa-thumbs-up"></i>
                    <span>{{ item.totalLikes }} 개</span>
                </div>
                <div>
                    <button @click="getCommentList(item)" class="comment-btn">댓글 {{ item.totalComments }}개</button>
                </div>
            </div>
            <div class="comment-wrapper">
                <input class="comment-input" type="text" placeholder="댓글을 입력하세요">
                <button id="button-id" class="comment-btn" @click="insertComment(item)">등록</button>
            </div>
            <BoardComment :board="item"/>
          </div>
        </div>
        <span id="goback">
            <button id="goback-btn" @click="backToFirst">처음으로</button>
        </span>
        </div>
    </div>
</div>
</template>

<script>
import BoardComment from '@/components/component/noAccess/Community/BoardComment.vue'
import {  mapActions, mapMutations, mapState } from 'vuex'
import editor from '../../global/editor.vue'

export default {
    name : 'Free',

    data(){
        return {
            updateContent : '',
            axiosState : false,
            isUpdate : false,
            isExport : 0,
            isReportClick : false,
            likeToggle : false,
        }
    },
    computed : {
        ...mapState({
            boardList : state=>state.community.boardList,
            updateCheck : state => state.community.updateCheck,
            numberOfArticle : state => state.community.numberOfArticle,
            articlesOnView : state => state.community.articlesOnView,
        })
    },

    methods: {
        ...mapActions({
            getBoardList : 'community/getBoardList',
            getBoardNum : 'community/getBoardNum',
            getMoreList : 'community/getMoreList',
            getComments : 'community/getComments'
        }),
        ...mapMutations({
            changeIsUpdate : 'community/changeIsUpdate',
            changeBoardIsModify : 'community/changeBoardIsModify',
            changeUpdateCheck : 'community/changeUpdateCheck',
        }),

        setLikeFlag(){
            this.likeToggle = !this.likeToggle
        },

        like(item){
            this.axios.post('url',null, { params : { idx : item.idx }})
                        .then(e => {
                            if(e > 0){
                                if(!this.likeToggle){
                                    item.좋아요 += 1
                                }
                            }
                        })
        },

        cancelLike(item){
            this.axios.post('url',null, { params : { idx : item.idx }})
                        .then(e =>  {
                            if(e > 0){
                                if(!this.likeToggle){
                                    item.좋아요 -= 1
                                }
                            }
                        })
        },

        changeIsReportClick() {
            this.isReportClick = !this.isReportClick
        },
        
        report(item){
            this.axios.get('url', null, { params : {idx : item.idx}})
                        .then(e => {
                            console.log(e)
                        })
        },
        
        cancelReport(item){
            
            this.axios.get('url', null, { params : {idx : item.idx}})
                        .then(e => {
                            console.log(e)
                        })
        },

        exportFinish(item) {
            this.changeIsUpdate(item); 
            this.changeBoardIsModify(item);
        },

        increasingIsExportUpdate(){
            this.isExport++
        },

        getArticle(e){  
            if(this.articlesOnView === this.numberOfArticle) {
                return
            }
            const fullSroll = e.target.scrollHeight
            const nowScroll = e.target.scrollTop

            if((fullSroll - nowScroll) < (fullSroll / 1.5) && !this.axiosState) {
                //원래는 이 부분에서 현재보여지는 게시글의 개수인 articlesOnView 같이 넘김
                //Controller에서 보여지는 개시글의 개수를 받아서 jpa문법으로 페이징처리를 위함
                //params : {articleNum : this.articleOnView}
                this.getMoreList()
            }
        },
        confirmDelete(item){
          if (confirm("해당 게시글을 정말 삭제하시겠습니까?")){
            this.deleteBoard(item)
          }
        },

        //게시판 삭제
        // token : sessionStorage.getItem('token')
        deleteBoard(item){
          this.axios
              .get('/DeleteBoard',{
                        params : {
                                  boardIdx : item.boardIdx,
                        }})
              .then(e =>{
                if(e.data === true){
                  item.delAt = 'Y'
                } else {
                  alert('삭제를 실패했습니다.')
                }
              })
        },


        //게시판 수정
        updateBoard(item){
            this.axios
                .put('',null, {params : {board : item,
                                content : this.updateContent,
                                token : sessionStorage.getItem('token')}})
                .then(() => {});
        },
        getCommentList(item) {
            if(item.댓글수 <= 0) {
                return
            }
            this.getComments(item)
        },
        //댓글 등록
        insertComment(item){
            const commentContent = document.querySelector('.comment-input')
            this.axios.post('url', null, { params :
                                            { idx : item.idx, commentContent : commentContent.value } })
              .then(() => {
                  commentContent.value = ''
              })
        },
        backToFirst(){
          document.querySelector('.router-wrapper').scroll(0,0)
        }

    },

    watch:{
        isExport(){
            let editor = document.querySelector('#content')
            let multipleFiles = document.querySelector('#multipleFiles')
                if(editor){
                    let _data = editor.innerHTML
                    let _files = multipleFiles.files
                    console.log(_data)
                    console.log(_files)
                }
        }
    },

  beforeMount() {
        this.getBoardNum()
    },

    components : {
        BoardComment,
        editor,
    },
}
</script>

<style scoped>
.board {
    width: 60vw;
    height: 80%;
    background-color: #2C2F3B;
    margin: 22px auto ;
    border-radius: 15px;
    color:white;
    padding: 30px;
}

.name-div {
    display: flex;
    justify-content: space-between;
    padding-top: 10px;
    padding-left: 20px;
    padding-right: 20px;
}

.icon-div {
    cursor: pointer;
}

.icon-container {
    display: flex;
    gap: 18px;
}

.content-div {
    height: fit-content;
    color: white;
    width: 100%;
}

.no-read-only {
    padding: 20px;
}

.read-only {
    margin-top: 20px;
    height: 100%;
}

.router-wrapper {
    overflow: scroll;
    height: calc(100vh - 100px);
   -ms-overflow-style: none; /* IE and Edge */
    scrollbar-width: none; /* Firefox */
}

.router-wrapper::-webkit-scrollbar {
    display: none;
}

#finish-div {
    color: white;
    font-weight: bold;
    cursor : pointer;
}

.comment-btn {
    color : black;

}

.comment-input {
    border-radius: 20px;
    background-color: #414556;
    height: 20px;
    color: #FFFFFF;
    padding-left : 14px;
    width: 100%;
    margin-left: 8px;
    outline: none;
    border: none;
    padding: 20px;
}

#btn-div {
    display: flex;
    justify-content: right;
    margin-bottom: 10px;
}

.comment-btn {
    background-color: #2C2F3B;
    border-radius: 10px;
    font-size: 13px;
    color: #fff;
    width: 70px;
}

#comment-insert-div {
    display: flex;
    justify-content: right;
    margin-top: 10px;
}

img {
    width: 15px;
    height: 15px;
    cursor: pointer;
}

.report-div {
    display: none;
}

#btn-div{
    font-size: 14px;
}

#btn-div > div:first-child{
    padding-right: 30px;
}

#btn-div > div > i{
    padding-right: 10px;
}

.clickedThumbs-up > i{
    color: #FF8906;
}

.comment-wrapper {
    display: flex;
    width: 100%;
}

#button-id {
    background: #FF8906;
    margin-left: 10px;
    height: 40px;
}

#goback {
  border-radius: 10px;
  background-color: coral;
  color: #fff;
  position: absolute;
  bottom: 10px;
  right: 13vw;
  z-index: 9999;
}

#goback-btn {
  color: #fff;
  padding: 5px;
}

.null-content {
  font-size: 20px;
  color: #fff;
}
</style>
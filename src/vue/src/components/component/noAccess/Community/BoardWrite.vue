<template>
        <editor :isExport="isExport" @exportContent="getContent"/>
        <div id="write-btn">
            <button type="button" @click="insert">등록</button>
        </div>
        {{ this.category }}
</template>

<script>
import editor from '../../global/editor.vue'

export default {

    name: 'App',
    data() {
        return {
            isExport: 0,
        }
    },
    props : {
        step : Number,
        category : String
    },
    components : {
        editor,
    },

    methods : {
        insert(){
            this.isExport++
        },

        exportContent(item) {
            console.log(item)
        },

        convertFileSize(fileSize){
          let str
          //MB 단위 이상일때 MB 단위로 환산
          if (fileSize >= 1024 * 1024) {
            fileSize = fileSize / (1024 * 1024);
            str = fileSize + ' MB';
          }
          //KB 단위 이상일때 KB 단위로 환산
          else if (fileSize >= 1024) {
            fileSize = fileSize / 1024;
            str = fileSize + ' KB';
          }
          //KB 단위보다 작을때 byte 단위로 환산
          else {
            str = fileSize + ' byte';
          }
          return str;
        },
        
        getContent(e) {
            let fileAt = ''
            let fileSize = ''
            let fileNameAndExtension = []
            let formData = new FormData()

            if(e._file !== ''){
              fileAt = 'Y'
              fileSize = this.convertFileSize(e._file.size)
              fileNameAndExtension = e._file.name.split('.')
              formData.append('file', e._file)
            } else {
              fileAt = 'N'
            }
            // const board = {
            //     //jwt에서 회원 고유번호 or Token 꺼내와서 담아주기
            //   token : "안뇽",
            //   boardCn : e._data,
            //   boardDate : '',
            //   delAt : "N",
            //   codeDetail : this.category,
            //   totalComments : 0,
            //   totalLikes : 0,
            //
            //   file : e._file,
            //   fileAt : fileAt,
            //   fileSize : fileSize,
            //   fileName : fileNameAndExtension[0],
            //   extensionName : fileNameAndExtension[1],
            // }
            this.axios.post('/insertBoard', null,
                          { params : {

                              token : "안뇽",
                              boardCn : e._data,
                              boardDate : '',
                              delAt : "N",
                              'codeDetail.codeDetailIdx' : this.category,
                              totalComments : 0,
                              totalLikes : 0,
                              fileAt : fileAt,

                              fileSize : fileSize,
                              fileName : fileNameAndExtension[0],
                              extensionName : fileNameAndExtension[1],
                          }})
                      .then(e => {

                          console.log(e)
                      })

            this.axios.post("/insertFile", formData,
                        { headers: { 'Content-Type': 'multipart/form-data' } })
                            .then(e => {
                              console.log(e)
                            })

        }
    },

}
</script>

<style scoped>
.write-div {
    color: #fff;
    height: 200px;
}
#write-btn button {
    color: #fff;
    background-color: coral;
    border-radius: 5px;
}
#write-btn {
    padding-top: 16px;
    color: #fff;
    display: flex;
    justify-content: right;
    margin-bottom: 20px;
}



</style>
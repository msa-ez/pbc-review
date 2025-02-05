<template>
    <div>
        <v-row class="pa-0 ma-0 align-center" v-if="!editMode">
            <v-avatar size="40">
                <v-img v-if="value.userImg && value.userImg.length > 0" :src="value.userImg"></v-img>
                <v-img v-else :src="require('@/assets/icon/user.svg')" width="48" height="48" class="mx-auto"></v-img>
            </v-avatar>
            <span class="ml-2">{{ value.userId ? value.userId : '사용자 ID' }}</span>
            <v-spacer></v-spacer>
            <div v-if="editable && !editMode">
                <v-btn icon text @click="edit">
                    <v-icon>mdi-pencil</v-icon>
                </v-btn>
                <v-btn icon text @click="remove">
                    <v-icon>mdi-delete</v-icon>
                </v-btn>
            </div>
        </v-row>
        <v-card-title class="pa-0" v-else>
            리뷰 작성
        </v-card-title>

        <v-card-text class="pa-0">
            <v-row class="ma-0 pa-0 align-center"
                :class="editMode ? 'mb-4' : ''"
            >
                <div v-if="editMode">별점 :</div>
                <v-rating
                    v-model="value.rating"
                    color="blue"
                    background-color="grey"
                    dense
                    :readonly="!editMode"
                    :size="!editMode ? '18' : ''"
                    length="5"
                ></v-rating>
            </v-row>
            <String label="리뷰" v-model="value.text" 
                :editMode="editMode" 
                :inputUI="'TEXTAREA'" 
                :showLabel="false"
                style="white-space: pre-wrap;"
            />
        </v-card-text>

        <v-card-actions class="pa-0" v-if="editMode">
            <v-spacer></v-spacer>
            <div>
                <v-btn v-if="isNew" color="primary" text @click="save">
                    추가
                </v-btn>
                <v-btn v-else color="primary" text @click="save">
                    저장
                </v-btn>
                <v-btn v-if="editMode && !isNew" color="primary" text @click="editMode = false">
                    취소
                </v-btn>
            </div>
        </v-card-actions>

        <v-snackbar
            v-model="snackbar.status"
            :top="true"
            :timeout="snackbar.timeout"
            color="error"
        >
            {{ snackbar.text }}
            <v-btn dark text @click="snackbar.status = false">
                Close
            </v-btn>
        </v-snackbar>
    </div>
</template>

<script>
const axios = require('axios').default;

export default {
    name: 'ReviewReview',
    props: {
        value: Object,
        isNew: Boolean,
        editable: Boolean,
    },
    data: () => ({
        editMode: false,
        snackbar: {
            status: false,
            timeout: 5000,
            text: '',
        },
    }),
    computed: {
        id() {
            if (this.value && this.value._links) {
                return this.value._links.self.href.split("/")[this.value._links.self.href.split("/").length - 1];
            }
            return '';
        }
    },
    created() {
        if (this.isNew) {
            this.editMode = true;
        }
    },
    methods: {
        selectFile(){
            if(this.editMode == false) {
                return false;
            }
            var me = this
            var input = document.createElement("input");
            input.type = "file";
            input.accept = "image/*";
            input.id = "uploadInput";
            
            input.click();
            input.onchange = function (event) {
                var file = event.target.files[0]
                var reader = new FileReader();

                reader.onload = function () {
                    var result = reader.result;
                    me.imageUrl = result;
                    me.value.userImg = result;
                };
                reader.readAsDataURL( file );
            };
        },
        edit() {
            this.editMode = true;
        },
        async save() {
            try {
                var temp = null;

                if(this.isNew) {
                    temp = await axios.post(axios.fixUrl('/reviews'), this.value)
                } else {
                    temp = await axios.put(axios.fixUrl('/reviews/' + this.id), this.value)
                }

                if(this.value != null) {
                    for(var k in temp.data) this.value[k]=temp.data[k];
                } else {
                    this.value = temp.data;
                }

                if (this.isNew) {
                    this.$emit('add');
                } else {
                    this.$emit('edit');
                    this.editMode = false;
                }
            } catch(e) {
                this.snackbar.status = true
                if(e.response && e.response.data.message) {
                    this.snackbar.text = e.response.data.message
                } else {
                    this.snackbar.text = e
                }
            }
        },
        async remove(){
            try {
                await axios.delete(axios.fixUrl('/reviews/' + this.id));
                if (this.editMode) {
                    this.editMode = false;
                }
                this.$emit('delete');
            } catch(e) {
                this.snackbar.status = true
                if(e.response && e.response.data.message) {
                    this.snackbar.text = e.response.data.message
                } else {
                    this.snackbar.text = e
                }
            }
        },
        change(){
            this.$emit('input', this.value);
        },
    },
}
</script>


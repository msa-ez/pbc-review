<template>
    <div>
        <v-row class="pa-0 ma-0 align-center" v-if="!editMode">
            <v-avatar size="40">
                <v-img v-if="value.userImg && value.userImg.length > 0" :src="value.userImg"></v-img>
                <v-img v-else :src="defaultUserImg" width="48" height="48" class="mx-auto"></v-img>
            </v-avatar>
            <span class="ml-2">{{ value.userId ? value.userId : '사용자 ID' }}</span>
            <v-spacer></v-spacer>
            <div v-if="editable && !editMode">
                <v-btn icon variant="text" @click="edit">
                    <v-icon>mdi-pencil</v-icon>
                </v-btn>
                <v-btn icon variant="text" @click="remove">
                    <v-icon>mdi-delete</v-icon>
                </v-btn>
            </div>
        </v-row>
        <v-card-title class="pa-0" v-else>
            리뷰 작성
        </v-card-title>

        <v-card-text class="pa-0">
            <v-row class="ma-0 mb-4 pa-0 align-center">
                <div v-if="editMode">별점 :</div>
                <v-rating
                    v-model="newValue.rating"
                    color="blue"
                    background-color="grey"
                    dense
                    :readonly="!editMode"
                    :size="!editMode ? '18' : ''"
                    length="5"
                ></v-rating>
            </v-row>
            <v-textarea
                v-model="newValue.text"
                label="리뷰"
                :readonly="!editMode"
                :variant="editMode ? 'outlined' : 'plain'"
                style="white-space: pre-wrap;"
                variant="outlined"
                auto-grow
                rows="2"
            ></v-textarea>
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
import axios from 'axios';
import userIcon from '@/assets/icon/user.svg';

export default {
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
        newValue: {
            itemId: '',
            rating: 0,
            text: '',
            userId: '',
            userImg: '',
        },
    }),
    computed: {
        id() {
            if (this.value && this.value._links) {
                return this.value._links.self.href.split("/")[this.value._links.self.href.split("/").length - 1];
            } else if (this.value.id) {
                return this.value.id;
            }
            return '';
        },
        defaultUserImg() {
            return userIcon;
        },
    },
    created() {
        if (this.isNew) {
            this.editMode = true;
        }
        this.newValue = this.value;
    },
    methods: {
        edit() {
            this.editMode = true;
        },
        async save() {
            try {
                if(this.isNew) {
                    await axios.post(axios.fixUrl('/reviews'), this.newValue)
                } else {
                    await axios.put(axios.fixUrl('/reviews/' + this.id), this.newValue)
                }

                if (this.isNew) {
                    this.$emit('add');
                    this.newValue.rating = 0;
                    this.newValue.text = '';
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


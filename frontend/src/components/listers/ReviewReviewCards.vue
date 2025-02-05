<template>
    <div>
        <div v-if="!detailMode">
            <v-row class="ma-0">
                <v-icon style="padding-bottom:2px;" color="blue" size="16">mdi-star</v-icon>
                <div class="ml-1" style="font-size:12px; color:gray">{{ averageRating }}</div>
                <div class="ml-1 mr-1" style="font-size:12px; font-weight: 900; color:gray">·</div>
                <div style="font-size:12px; color:gray">리뷰 {{ values.length }}</div>
            </v-row>
        </div>
        <div v-else>
            <div class="pa-4 d-flex justify-center">
                <div class="text-center mr-8">
                    <div style="font-size:64px; font-weight: 500;">{{ averageRating }}</div>
                    <v-rating
                        v-model="averageRating"
                        color="blue"
                        background-color="grey"
                        dense
                        readonly
                        length="5"
                    ></v-rating>
                </div>
                <div class="mr-8">
                    <div v-for="(count, rating) in ratingCounts" :key="rating">
                        <v-row class="ma-0 pa-0 justify-center align-center">
                            <div style="font-weight: 700;">{{ rating }}점</div>
                            <div class="ml-2 mr-2">
                                <v-progress-linear
                                    style="width:100px;"
                                    :value="(count / values.length) * 100"
                                    height="5"
                                    color="blue"
                                    rounded
                                ></v-progress-linear>
                            </div>
                            <div style="opacity: 0.7;">{{ count }}</div>
                        </v-row>
                    </div>
                </div>
                <div class="text-center align-center">
                    <div style="font-size:12px; font-weight: 700;">전체 리뷰수</div>
                    <v-img :src="require('@/assets/icon/chat.svg')" width="48" height="48" class="mx-auto"></v-img>
                    <div style="font-size:32px; font-weight: 700;">{{ values.length }}</div>
                </div>
            </div>
                
            <div style="width: 100%;">
                <div v-if="showReviews">
                    <v-col class="pa-0" v-for="(review, index) in values" :key="index">
                        <ReviewReview 
                            :isNew="false" 
                            :value="review"
                            :editable="review.userId == userId" 
                            @delete="remove" 
                            @edit="edit" 
                            class="mx-auto mb-4 mt-4 pa-4" />
                        <v-divider></v-divider>
                    </v-col>
                </div>
                <v-col v-if="showReviewInput" class="pa-0 pt-4">
                    <ReviewReview :isNew="true" v-model="newValue" @add="append" class="mx-auto pa-4" />
                </v-col>
            </div>
        </div>
    </div>
</template>

<script>
const axios = require('axios').default;
import ReviewReview from './../ReviewReview.vue';

export default {
    name: 'ReviewReviewCards',
    components: {
        ReviewReview,
    },
    props: {
        value: Object,
        // showReviews 등록된 리뷰 보기,
        showReviews: {
            type: Boolean,
            default: false, // 기본값 설정
        },
        // showReviewInput 리뷰 등록 UI 활성화
        showReviewInput: {
            type: Boolean,
            default: false, // 기본값 설정
        },
        //detailMode (false : 별점 평균과 등록된 리뷰만 표시, true : showReviews, showReviewInput 활성화시 디테일 UI로 변경)
        detailMode: {
            type: Boolean,
            default: false, // 기본값 설정
        },
    },
    data: () => ({
        values: [],
        filteredValues: [], // 필터링된 리뷰 목록
        selectedRating: null, // 선택된 별점
        newValue: {
            'itemId': '',
            'rating': 0,
            'text': '',
            'userId': '',
            'userImg': '',
        },
        openDialog: false,
        averageRating: 0,
        ratingCounts: { 1: 0, 2: 0, 3: 0, 4: 0, 5: 0 },
    }),
    computed: {
        userId() {
            if (this.value && this.value.userId) return this.value.userId;
            return null
        }
    },
    created() {
        var me = this;
        if (this.value) {
            this.newValue = {
                'itemId': this.value.itemId || '',
                'rating': 0,
                'text': '',
                'userId': this.value.userId || '',
                'userImg': this.value.userImg || null,
            };
        }
        me.getReviewList();
    },
    methods:{
        async getReviewList() {
            var me = this;
            try {
                var temp = null;
                var url = '/reviews';
                if (me.value && me.value.itemId) {
                    url = '/reviews/search/findByItemId?itemId=' + me.value.itemId;
                }
                temp = await axios.get(axios.fixUrl(url));
                if (temp.data) {
                    me.values = temp.data._embedded.reviews;
                    me.calculateAverageRating();
                } else {
                    me.values = [];
                }
            } catch(e) {
                console.log(e);
            }
        },
        append() {
            this.getReviewList();
            this.calculateAverageRating();
            this.newValue = {
                'itemId': this.value.itemId || '',
                'rating': 0,
                'text': '',
                'userId': this.value.userId || '',
                'userImg': this.value.userImg || null,
            };
        },
        remove() {
            this.getReviewList();
            this.calculateAverageRating(); // 데이터 변경 후 평균 계산
        },
        edit() {
            this.getReviewList();
            this.calculateAverageRating(); // 데이터 변경 후 평균 계산
        },
        calculateAverageRating() {
            if (this.values.length === 0) {
                this.averageRating = 0;
                this.ratingCounts = { 1: 0, 2: 0, 3: 0, 4: 0, 5: 0 };
                return;
            }
            const total = this.values.reduce((sum, review) => sum + (review.rating || 0), 0);
            this.averageRating = parseFloat((total / this.values.length).toFixed(1));

            // 각 점수별 제출자 수 계산
            this.ratingCounts = { 1: 0, 2: 0, 3: 0, 4: 0, 5: 0 };
            this.values.forEach(review => {
                if (review.rating >= 1 && review.rating <= 5) {
                    this.ratingCounts[review.rating]++;
                }
            });
        },
    }
};
</script>

<style>
</style>


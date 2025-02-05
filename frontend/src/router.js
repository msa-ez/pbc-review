
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

import ReviewReviewManager from "./components/listers/ReviewReviewCards"
import ReviewReviewDetail from "./components/listers/ReviewReviewDetail"

export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            alias: '/reviews/reviews',
            component: ReviewReviewManager
        },
        {
            path: '/reviews/reviews',
            name: 'ReviewReviewManager',
            component: ReviewReviewManager
        },
        {
            path: '/reviews/reviews/:id',
            name: 'ReviewReviewDetail',
            component: ReviewReviewDetail
        },
    ]
})

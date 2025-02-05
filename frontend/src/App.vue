<template>
    <v-app>
        <v-main>
            <v-container fluid class="pa-0">
                <!-- <component v-bind:is="componentName" :value="value"></component> -->
                <review-review-cards
                    :show-reviews="showReviews" 
                    :show-review-input="showReviewInput" 
                    :detail-mode="detailMode"
                ></review-review-cards>
            </v-container>
        </v-main>
    </v-app>
</template>

<script>
import ReviewReviewCards from "./components/listers/ReviewReviewCards"
// import ReviewReviewDetail from "./components/listers/ReviewReviewDetail"

export default {
    name: "App",
    components: {
        "review-review-cards": ReviewReviewCards,
        // "review-review-detail": ReviewReviewDetail
    },
    props: {
        value: Object
    },
    data() {
        return {
            componentName: "review-review-manager",
            showReviews: true,
            showReviewInput: true,
            detailMode: true
        }
    },
    mounted() {
        // if (this.value) {
        //     if (this.value.id) {
        //         this.componentName = "review-review-detail";
        //     } else {
        //         this.componentName = "review-review-manager";
        //     }
        // }
    },
};
</script>


<style>
</style>
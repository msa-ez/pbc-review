<template>
    <div>
        <review-review-cards
            :show-reviews="showReviews" 
            :show-review-input="showReviewInput" 
            :detail-mode="detailMode"
            :average-mode="averageMode"
        ></review-review-cards>
    </div>
</template>

<script>
export default {
    props: {
        value: Object
    },
    data() {
        return {
            showReviews: true,
            showReviewInput: true,
            detailMode: true,
            averageMode: true,
        }
    },
};
</script>
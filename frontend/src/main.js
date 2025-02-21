import { createApp } from 'vue';
import vuetify from './plugins/vuetify';
import './GlobalStyle.css';
import App from "./App.vue";
import axios from 'axios';

import ReviewReviewCards from './components/ReviewReviewCards.vue';
import ReviewReview from './components/ReviewReview.vue';

axios.fixUrl = function(original) {
    if (!axios.backend && original.indexOf("/") === 0) {
        return original;
    }

    let url = null;
    try {
        url = new URL(original);
    } catch (e) {
        url = new URL(axios.backend + original);
    }

    if (!axios.backend) return url.pathname;

    url.hostname = axios.backendUrl.hostname;
    url.port = axios.backendUrl.port;

    return url.href;
};

createApp(App)
    .use(vuetify)
    .component('review-review-cards', ReviewReviewCards)
    .component('review-review', ReviewReview)
    .mount('#app');

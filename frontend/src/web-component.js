import { createApp, h } from 'vue';
import vuetify from './plugins/vuetify';
import './GlobalStyle.css';
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

class WebComponentElement extends HTMLElement {
    constructor() {
        super();
        this.vueInstance = null;
    }

    connectedCallback() {
        // Map of registered components
        const componentMap = {
            'review-review-cards': ReviewReviewCards,
            'review-review': ReviewReview
        };

        // Collect child components to render
        const childComponents = Array.from(this.childNodes)
            .filter(node => node.nodeType === Node.ELEMENT_NODE)
            .map(node => {
                const tagName = node.tagName.toLowerCase();
                const component = componentMap[tagName];
                if (component) {
                    const props = {};
                    if (node.attributes) {
                        Array.from(node.attributes).forEach(attr => {
                            try {
                                props[attr.name] = JSON.parse(attr.value);
                            } catch (e) {
                                props[attr.name] = attr.value;
                            }
                        });
                    }

                    return h(component, { ...props });
                } else {
                    console.warn(`Component for tag <${tagName}> not found.`);
                    return null;
                }
            }).filter(Boolean);

        this.vueInstance = createApp({
            render() {
                return h('div', childComponents);
            }
        });

        this.vueInstance.use(vuetify);
        this.vueInstance.component('review-review-cards', ReviewReviewCards);
        this.vueInstance.component('review-review', ReviewReview);
        this.vueInstance.mount(this);
    }

    disconnectedCallback() {
        if (this.vueInstance) {
            this.vueInstance.unmount(); // Correct method to unmount in Vue 3
        }
    }
}

window.customElements.define('review-app', WebComponentElement);

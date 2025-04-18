# review-app

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run dev
```

### Compiles and minifies for web component registration.
```
npm run build
```
Check the review-app.js file was created in the dist directory

### Serves the built files on port 8050 for local access.
```
npm run start
```

## How to use web components for other projects

### Before Using Web Components
Review backend microservice and review-app Web Component must be running.
- You must register a review backend microservice with the API gateway that you are currently running.
```
-- Example of API Gateway application.yml
spring:
  profiles: default
  cloud:
    gateway:
      routes:
#<<< Add review backend microservice here
        - id: review-backend
          uri: http://localhost:8052
          predicates:
            - Path=/reviews/**
#>>> Warning: The review backend must not duplicate with other service paths.
        - id: other-services
          uri: http://other-services:8080
          predicates:
            - Path=/**
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true
```


### Load built web component files and required libraries
- Add the libraries, Vuetify, VueJs, to the &lt;head&gt; tag.
- Add the built review-app web component to the &lt;body&gt; tag.
```
-- index.html
<head>
    <!-- Vuetify, VueJs -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/vuetify@3.x/dist/vuetify.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@mdi/font@latest/css/materialdesignicons.min.css" />
    <link rel="stylesheet" href="https://fonts.bunny.net/css?family=roboto:400,500,700" />
    <script type="importmap">
    {
      "imports": {
        "vue": "https://cdn.jsdelivr.net/npm/vue@latest/dist/vue.esm-browser.js",
        "vuetify": "https://cdn.jsdelivr.net/npm/vuetify@3.x/dist/vuetify.esm.js"
      }
    }
    </script>
</head>
<body>
    <!-- built Web Components file -->
    <script src="http://localhost:8050/review-app.js"></script>
</body>
```

### Using Example
Built Web Components can be used as HTML tags.
- The component name and props you want to use must be written in a kebab case.
- If the prop type you want to deliver is JSON Object, you must convert it to a string and deliver it.
```
<template>
    <review-app>
        <!-- The JSON Object must be converted to a string using JSON.stringify() -->
        <review-review-cards
            :value="JSON.stringify(reviewData)"
            :show-reviews="showReviews" 
            :show-review-input="showReviewInput" 
            :detail-mode="detailMode"
            :average-mode="averageMode"
        ></review-review-cards>
    </review-app>
</template>

<script>
export default {
    data: () => ({
        // Review data
        reviewData: {
          itemId: 1,
          userId: "user1",
        },
        showReviews: true,  // Show registered reviews
        showReviewInput: true,  // Activate review registration UI
        detailMode: true, // Detailed view (false: shows average rating and registered reviews only, true: changes to detailed UI when showReviews and showReviewInput are activated)
        averageMode: true // Show overall review average
    })
}
</script>
```

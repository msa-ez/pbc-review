# review-app

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
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

### Lints and fixes files
```
npm run lint
```

## How to use web components for other projects

### Before Using Web Components
Review backend microservice and review-app Web Component must be running.
- You must register a review backend microservice with the API gateway that you are currently running.
```
-- Example of API Gateway application.yml
server:
  port: 8088

---

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
    <link href="https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700,900" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/@mdi/font@6.x/css/materialdesignicons.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/vuetify@2.x/dist/vuetify.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/vuetify@2.x/dist/vuetify.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vue@2.x/dist/vue.min.js"></script>
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
        ></review-review-cards>
    </review-app>
</template>

<script>
export default {
    name: "App",
    data: () => ({
        reviewData: {`
            'rating': 5,
            'content': 'Very Good'
        },
        showReviews: true,
        showReviewInput: true,
        detailMode: true
    })
}
</script>
```

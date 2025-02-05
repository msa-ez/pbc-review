module.exports = {
  transpileDependencies: [
    'vuetify'
  ],
  devServer: {
    disableHostCheck: true
  },
  configureWebpack: {
    output: {
      libraryTarget: 'window',
      filename: 'review-app.js',
      libraryExport: 'default',
    },
  },
  chainWebpack: config => {
    config.module
      .rule('vue')
      .use('vue-loader')
      .loader('vue-loader')
      .tap(options => {
        options.compilerOptions = {
          ...options.compilerOptions,
          isCustomElement: tag => tag === 'review-app',
        };
        return options;
      });
  }
}

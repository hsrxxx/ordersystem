const path = require('path')

const resolve = dir => {
    return path.join(__dirname, dir)
}

module.exports = {
    publicPath: "./",
    devServer: {
        port: 8080,
        proxy: {
            "/": {
                target: 'http://localhost:9812',
                changeOrigin: true,
                pathRewrite: {
                    "^/": ""
                }
            },
        }
    },
    chainWebpack: config => {
        config.resolve.alias
          .set("@", resolve("src"))
        //   .set("assets", resolve("src/assets"))
        //   .set("components", resolve("src/components"))
        //   .set("base", resolve("baseConfig"))
        //   .set("public", resolve("public"));
      },
}

var path = require("path");
var ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
    entry: {
        app: [
            './source/index.js'
        ]
    },

    output: {
        path: path.resolve(__dirname + '/dist'),
        filename: '[name].js',
    },

    module: {
        loaders: [
            //            {
            //                test: /\.(css|scss)$/,
            //                loaders: ['style', 'css', 'sass']
            //            },
            {
                test: /\.(css|scss)$/,
                loader: ExtractTextPlugin.extract('css!sass')
            }, {
                test: /\.html$/,
                exclude: /node_modules/,
                loader: 'file?name=[name].[ext]',
            }, {
                test: /\.elm$/,
                exclude: [/elm-stuff/, /node_modules/],
                loader: 'elm-webpack',
            }, {
                test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
                loader: 'url-loader?limit=10000&mimetype=application/font-woff',
            }, {
                test: /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
                loader: 'file-loader',
            },
        ],

        noParse: /\.elm$/,
    },
    resolve: {
        extensions: ['', '.js', '.scss'],
        root: [path.join(__dirname, './source')]
    },
    plugins: [
        new ExtractTextPlugin('main.css', {
            allChunks: true
        })
    ],
    sassLoader: {
        includePaths: [path.resolve(__dirname, "./source/stylesheets")]
    },
    devServer: {
        inline: true,
        stats: {
            colors: true
        },
    },

};
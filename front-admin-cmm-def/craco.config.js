const path = require('path');
const { getLoader, loaderByName } = require('@craco/craco');
const CracoAlias = require('craco-alias');
const webpack = require('webpack');
module.exports = {
  devServer: {
    host: 'pc.atops.or.kr',
    port: 5500,
    // port: 8086,
  },
  plugins: [
    {
      plugin: CracoAlias,
      options: {
        source: 'options',
        baseUrl: './src',
        aliases: {
          '~': '.',
          shared: '../../shared/src',
        },
      },
    },
  ],
  babel: {
    presets: ['@emotion/babel-preset-css-prop'],
    plugins: [
      [
        '@emotion',
        {
          sourceMap: false,
          autoLabel: 'dev-only',
          labelFormat: '[local]',
          cssPropOptimization: true,
        },
      ],
    ],
  },
  webpack: {
    configure: (config, { env, paths }) => {
      const { isFound, match } = getLoader(
        config,
        loaderByName('babel-loader')
      );

      if (isFound) {
        const include = Array.isArray(match.loader.include)
          ? match.loader.include
          : [match.loader.include];
        match.loader.include =
          include.concat[[path.join(__dirname, '../shared')]];
      }

      config.resolve.plugins = config.resolve.plugins.filter((plugin) => {
        return plugin.constructor.name !== 'ModuleScopePlugin';
      });
      return config;
    },
    plugins: [
      new webpack.DefinePlugin({
        __DEV__: process.env.REACT_APP_MODE === 'dev',
        __PROD__: process.env.REACT_APP_MODE === 'prod',
        __STAGE__: process.env.REACT_APP_MODE === 'stage',
      }),
    ],
  },
};

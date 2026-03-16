'use strict'
require('./check-versions')()

// process.env.NODE_ENV = 'production'

const ora = require('ora')
const rm = require('rimraf')
const path = require('path')
const chalk = require('chalk')
const webpack = require('webpack')
const config = require('../config')
const webpackConfig = require('./webpack.prod.conf')

const spinner = ora('building for production...')
spinner.start()

var fs = require('fs');

fs.readFile('package.json', (err, data) => {
    if (err) throw err;

    var packageJsonObj = JSON.parse(data);
    var versionNumber = packageJsonObj.version;
    packageJsonObj.version = versionNumber.substring(0,versionNumber.lastIndexOf('.')) + '.' + (versionNumber.substring(versionNumber.lastIndexOf('.')+1)*1+1);//`${versionNumber}-${build}`;
    packageJsonObj = JSON.stringify(packageJsonObj,null,2);

    fs.writeFile('package.json', packageJsonObj, (err) => {
        if (err) throw err;
        console.log('Updated version!');
    });
});

rm(path.join(config.build.assetsRoot, config.build.assetsSubDirectory), err => {
  if (err) throw err
  webpack(webpackConfig, (err, stats) => {
    spinner.stop()
    if (err) throw err
    process.stdout.write(stats.toString({
      colors: true,
      modules: false,
      children: false, // If you are using ts-loader, setting this to true will make TypeScript errors show up during build.
      chunks: false,
      chunkModules: false
    }) + '\n\n')

    if (stats.hasErrors()) {
      console.log(chalk.red('  Build failed with errors.\n'))
      process.exit(1)
    }

    console.log(chalk.cyan('  Build complete.\n'))
    console.log(chalk.yellow(
      '  Tip: built files are meant to be served over an HTTP server.\n' +
      '  Opening index.html over file:// won\'t work.\n'
    ))
  })
})

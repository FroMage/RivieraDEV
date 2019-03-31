'use strict';

const SCSS_DIR = './public/stylesheets';

var gulp = require('gulp');
var sass = require('gulp-sass');

sass.compiler = require('node-sass');

gulp.task('sass', function() {
    return gulp
        .src(SCSS_DIR + '/**/*.scss')
        .pipe(sass().on('error', sass.logError))
        .pipe(gulp.dest('./css'));
});

gulp.task('sass:watch', function() {
    gulp.watch(SCSS_DIR + '/**/*.scss', ['sass']);
});

gulp.task('default', ['sass:watch']);

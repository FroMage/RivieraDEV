'use strict';

const STYLE_DIR = './public/stylesheets';

const gulp = require('gulp');
const sass = require('gulp-sass');
const autoprefixer = require('gulp-autoprefixer');
const merge = require('merge-stream');
const runSequence = require('run-sequence');
const wait = require('gulp-wait');

sass.compiler = require('node-sass');

gulp.task('sass', function() {
    return gulp
        .src(STYLE_DIR + '/**/*.scss')
        .pipe(wait(200))
        .pipe(sass.sync().on('error', sass.logError))
        .pipe(gulp.dest(STYLE_DIR));
});

gulp.task('autoprefixer', function() {
    const files = ['style.css', 'live.css'];
    let tasks = files.map(file => {
        return gulp
            .src(`${STYLE_DIR}/${file}`)
            .pipe(
                autoprefixer({
                    browsers: ['last 2 versions'],
                    cascade: false,
                })
            )
            .pipe(gulp.dest(STYLE_DIR));
    });

    return merge(tasks);
});

gulp.task('sass:watch', function() {
    gulp.watch(STYLE_DIR + '/**/*.scss', () => {
        runSequence('sass', 'autoprefixer');
    });
});

gulp.task('default', ['sass:watch']);

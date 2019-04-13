'use strict';

const STYLE_DIR = './public/stylesheets';

const gulp = require('gulp');
const sass = require('gulp-sass');
const autoprefixer = require('gulp-autoprefixer');
const merge = require('merge-stream');
const runSequence = require('run-sequence');
const wait = require('gulp-wait');
const responsive = require('gulp-responsive'); // To generate WebP images

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

gulp.task('responsive-images', function() {
    return gulp
        .src('public/images/illustrations/*-mini.jpg')
        .pipe(
            responsive(
                {
                    // Resize all JPG images to three different sizes: 667 pixel 768pixels and default size
                    '*.jpg': [
                        {
                            width: 667,
                            format: 'jpg',
                            progressive: true,
                            rename: { suffix: '-mobile' },
                        },
                        {
                            width: 667,
                            format: 'webp',
                            rename: { suffix: '-mobile' },
                        },
                        {
                            width: 768,
                            format: 'jpg',
                            progressive: true,
                            rename: { suffix: '-tablet' },
                        },
                        {
                            width: 768,
                            format: 'webp',
                            rename: { suffix: '-tablet' },
                        },
                        {
                            format: 'jpg',
                            progressive: true,
                            rename: { suffix: '-desktop' },
                        },
                        {
                            format: 'webp',
                            rename: { suffix: '-desktop' },
                        },
                    ],
                },
                {
                    // Global configuration for all images
                    // Strip all metadata
                    withMetadata: false,
                }
            )
        )
        .pipe(gulp.dest('public/images/illustrations/responsives/'));
});

gulp.task('default', ['sass:watch']);

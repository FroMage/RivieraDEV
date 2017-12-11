let gulp = require('gulp');
let compass = require('gulp-compass');

const SCSS_DIR = './public/stylesheets';

// Compile SASS to CSS by Compass
gulp.task('compass', function() {
 gulp.src(SCSS_DIR)
   .pipe(compass({
     css: SCSS_DIR,
     sass: SCSS_DIR
   }));
});

//Watch task
gulp.task('default',function() {
    gulp.watch(SCSS_DIR + '/**/*.scss',['compass']);
});
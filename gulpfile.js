const gulp = require("gulp");
const concat = require("gulp-concat");

//需要合并文件时使用
gulp.task("pipe",function () {
    gulp.src([""]).pipe(concat("")).pipe(gulp.dest(""));
});
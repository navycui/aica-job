export const scrollY = () => {
  var supportPageOffset = window.pageXOffset !== undefined;
  var isCSS1Compat = (document.compatMode || '') === 'CSS1Compat';
  var y = supportPageOffset
    ? window.pageYOffset
    : isCSS1Compat
    ? document.documentElement.scrollTop
    : document.body.scrollTop;
  return Math.floor(y);
};

export default {
  scrollY,
};

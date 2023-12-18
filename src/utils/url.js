export function getQueryString(name) {
  const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  const urlObj = window.location;
  var r =
    urlObj.href.indexOf("#") > -1
      ? urlObj.hash.split("?")[1].match(reg)
      : urlObj.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]);
  return null;
}

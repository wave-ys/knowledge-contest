import constants from "@/constants";

export function getDepartmentById(id) {
  for (const value of constants.DEPARTMENT) {
    if (value.id == id) return value.name;
  }
  return "无法识别";
}

export function getDepartmentByPrefix(prefix) {
  for (const value of constants.DEPARTMENT) {
    if (value.prefix.indexOf(prefix) != -1) return value.name;
  }
  return "无法识别";
}

export function getDepartmentBySid(Sid) {
  const tmp = getDepartmentByPrefix(Sid.substring(0, 3));
  if (tmp != "无法识别") return tmp;
  else return getDepartmentByPrefix(Sid.substring(0, 2));
}

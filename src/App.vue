<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import departmentApi from "@/api/department";
import constants from "@/constants";

export default {
  name: "App",
  async created() {
    constants.DEPARTMENT = await departmentApi.get();
  }
};
</script>

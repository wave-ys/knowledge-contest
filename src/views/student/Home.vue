<template>
  <router-view></router-view>
</template>

<script>
export default {
  created() {
    if (this.$store.state.user.id == -1) this.$store.dispatch("user/getInfo");
    if (this.$route.name == "exam") this.$router.replace("/student/index");
  }
};
</script>

<style></style>

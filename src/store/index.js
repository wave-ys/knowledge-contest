import Vue from "vue";
import Vuex from "vuex";
import getters from "./getters";
import user from "./modules/users";

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    user
  },
  getters
});

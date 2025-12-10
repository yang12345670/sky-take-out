import Vue from "vue";
import Router from "vue-router";
import Layout from "@/layout/index.vue";
import {
  getToken,
  setToken,
  removeToken,
  getStoreId,
  setStoreId,
  removeStoreId,
  setUserInfo,
  getUserInfo,
  removeUserInfo
} from "@/utils/cookies";
import store from "@/store";

Vue.use(Router);

const router = new Router({
  scrollBehavior: (to, from, savedPosition) => {
    if (savedPosition) {
      return savedPosition;
    }
    return { x: 0, y: 0 };
  },
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/login",
      component: () =>
        import(/* webpackChunkName: "login" */ "@/views/login/index.vue"),
      meta: { title: "EAT NYC", hidden: true, notNeedAuth: true }
    },
    {
      path: "/404",
      component: () => import(/* webpackChunkName: "404" */ "@/views/404.vue"),
      meta: { title: "EAT NYC", hidden: true, notNeedAuth: true }
    },
    {
      path: "/",
      component: Layout,
      redirect: "/dashboard",
      children: [
        {
          path: "dashboard",
          component: () =>
            import(/* webpackChunkName: "dashboard" */ "@/views/dashboard/index.vue"),
          name: "Dashboard",
          meta: {
            title: "Dashboard",
            icon: "dashboard",
            affix: true
          }
        },
        {
          path: "order",
          component: () =>
            import(/* webpackChunkName: "shopTable" */ "@/views/orderDetails/index.vue"),
          meta: {
            title: "Order Management",
            icon: "icon-order"
          }
        },
        {
          path: "category",
          component: () =>
            import(/* webpackChunkName: "shopTable" */ "@/views/category/index.vue"),
          meta: {
            title: "Category Management",
            icon: "icon-category"
          }
        },
        {
          path: "dish",
          component: () =>
            import(/* webpackChunkName: "shopTable" */ "@/views/dish/index.vue"),
          meta: {
            title: "Dish Management",
            icon: "icon-dish"
          }
        },
        {
          path: "/dish/add",
          component: () =>
            import(/* webpackChunkName: "shopTable" */ "@/views/dish/addDishtype.vue"),
          meta: {
            title: "Add Dish Type",
            hidden: true
          }
        },
        {
          path: "setmeal",
          component: () =>
            import(/* webpackChunkName: "shopTable" */ "@/views/setmeal/index.vue"),
          meta: {
            title: "Set meal Management",
            icon: "icon-combo"
          }
        },
        {
          path: "/statistics",
          component: () =>
            import(/* webpackChunkName: "shopTable" */ "@/views/statistics/index.vue"),
          meta: {
            title: "Statistics",
            icon: "icon-statistics"
          }
        },
        {
          path: "employee",
          component: () =>
            import(/* webpackChunkName: "shopTable" */ "@/views/employee/index.vue"),
          meta: {
            title: "Employee Management",
            icon: "icon-employee"
          }
        },

        {
          path: "/employee/add",
          component: () =>
            import(/* webpackChunkName: "dashboard" */ "@/views/employee/addEmployee.vue"),
          meta: {
            title: "Add Employee",
            hidden: true
          }
        },

        {
          path: "/setmeal/add",
          component: () =>
            import(/* webpackChunkName: "shopTable" */ "@/views/setmeal/addSetmeal.vue"),
          meta: {
            title: "Add Set Meal",
            hidden: true
          }
        },


        {
          path: "inform",
          component: () =>
            import(/* webpackChunkName: "shopTable" */ "@/views/inform/index.vue"),
          meta: {
            title: "Notifications",
            icon: "icon-inform"
          }
        }
      ]
    },
    {
      path: "*",
      redirect: "/404",
      meta: { hidden: true }
    }
  ]
});

export default router;

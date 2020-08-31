import router from './router';
import store from './store';
import { getToken, getUserId } from '@/utils/auth';
import { Toast, Indicator } from 'mint-ui';

const whiteList = ['/login', '/register'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // 显示页面加载效果
  Indicator.open()
  // determine whether the user has logged in
  const hasToken = getToken() || '';

  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      setTimeout(() => Indicator.close(), 50);
    } else {
      const userInfo = store.getters.info;
      if (userInfo) {
        next()
      } else {
        try {
          // get user info
          await store.dispatch('user/getInfo', getUserId())

          next()
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          Toast({
            message: error.message || 'Error',
            duration: 5 * 1000
          })
          next(`/login?redirect=${to.path}`)
          Indicator.close();
        }
      }
    }
  } else {
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next();
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next({path: `/login?redirect=${to.path}`});
    }
  }
})

router.afterEach(async () => {
  // finish progress bar
  setTimeout(() => Indicator.close(), 50);
})

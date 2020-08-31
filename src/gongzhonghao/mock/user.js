import Mock from 'mockjs'

const users = {
  '小明': {
    name: '小明',
    phone: '15029345678',
    address: '嘉兴市南湖区'
  }
}

const userData = Mock.mock({
  'items|100': [{
    name: '@cname',
    'role|1': ['超级管理员', '市级', '县级'],
    account: '@id',
    date: '@datetime',
    company: () => '@city 园林规划院',
    address: '@county(true)'
  }]
})

export default [
  // user login
  {
    url: '/user/login',
    type: 'post',
    response: config => {
      const { user_name, user_password } = config.query
      if (user_name != '小明') {
        return {
          code: 0,
          message: '用户明不存在'
        }
      }
      if (user_password != '123456') {
        return {
          code: 0,
          message: '密码错误'
        }
      }
      const userInfo = users[user_name]
      return {
        code: 20000,
        data: userInfo
      }
    }
  },

  {
    url: '/user/getInfo',
    type: 'get',
    response: config => {
      const { token } = config.query;
      const info = users[token];
      return {
        code: 20000,
        data: info
      }
    }
  },

  // get user info
  {
    url: '/user/info\.*',
    type: 'get',
    response: config => {
      const { token } = config.query
      const info = users[token]

      // mock error
      if (!info) {
        return {
          code: 50008,
          message: 'Login failed, unable to get user details.'
        }
      }

      return {
        code: 20000,
        data: info
      }
    }
  },
  {
    url: '/user/list',
    type: 'get',
    response: config => {
      const { currentPage, pageSize } = config.query;
      const start = (Number(currentPage) - 1) * Number(pageSize);
      const end = start + Number(pageSize);
      const items = userData.items
      return {
        code: 20000,
        data: {
          total: items.length,
          items: items.slice(start, end)
        }
      }
    }
  },
  // user logout
  {
    url: '/user/logout',
    type: 'post',
    response: _ => {
      return {
        code: 20000,
        data: 'success'
      }
    }
  }
]

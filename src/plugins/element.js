import Vue from "vue";
import {
  Button,
  Form,
  FormItem,
  Input,
  Icon,
  Card,
  Header,
  Container,
  Aside,
  Menu,
  MenuItem,
  Main,
  PageHeader,
  Dropdown,
  DropdownMenu,
  DropdownItem,
  Row,
  Table,
  TableColumn,
  Tag,
  Dialog,
  MessageBox,
  Message,
  Loading,
  Col,
  Select,
  Option,
  Pagination,
  Footer,
  ButtonGroup
} from "element-ui";

const msgbox = MessageBox;
const loading = Loading;

Vue.use(Button);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Input);
Vue.use(Icon);
Vue.use(Card);
Vue.use(Header);
Vue.use(Container);
Vue.use(Aside);
Vue.use(Menu);
Vue.use(MenuItem);
Vue.use(Main);
Vue.use(PageHeader);
Vue.use(Dropdown);
Vue.use(DropdownMenu);
Vue.use(DropdownItem);
Vue.use(Row);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(Tag);
Vue.use(Dialog);
Vue.use(Col);
Vue.use(Select);
Vue.use(Option);
Vue.use(Pagination);
Vue.use(Footer);
Vue.use(loading.directive);
Vue.use(ButtonGroup);

Vue.prototype.$msgbox = msgbox;
Vue.prototype.$alert = msgbox.alert;
Vue.prototype.$confirm = msgbox.confirm;
Vue.prototype.$prompt = msgbox.prompt;
Vue.prototype.$message = Message;
Vue.prototype.$loading = loading.service;

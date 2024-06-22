# cloud_storage
### 一、项目概述

本项目旨在开发一个简洁高效的云存储解决方案，用户可以通过Web界面管理自己的文件。系统将提供基本的文件操作功能，如查看、上传、下载，并支持通过配置管理文件存储的位置。

#### 1. 核心功能

1. **文件列表**
   - 实现一个基于Web的界面，用户可以查看当前云盘中的文件和目录结构。
   - 界面通过HTML列表标签展示文件和目录，无需复杂的样式设计。
2. **文件下载**
   - 用户可以选择云盘中的文件进行下载。
   - 系统需要处理文件下载请求，并确保文件数据的完整性和安全性。
3. **文件上传**
   - 允许用户上传文件到指定的云盘目录。
   - 需要考虑文件上传过程中的数据安全性和传输稳定性。
4. **文件存储位置配置**
   - 通过SpringBoot的配置文件，管理员可以配置文件存储的根目录。
   - 这允许系统适应不同的存储需求和硬件环境。
5. **本地文件挂载**
   - 支持通过Docker命令将云盘目录挂载至宿主机目录。
   - 这为在本地服务器上备份或直接访问云盘数据提供便利。

#### 2. 技术栈

- **后端：**
  - **SpringBoot**：作为主要的后端框架，处理业务逻辑和服务器端操作。利用其自动配置、依赖注入和内置服务器特性简化开发和部署过程。
- **前端：**
  - **HTML5/CSS**：构建用户界面的基础结构和样式。
  - **JavaScript**：用于页面的动态交互。包括原生JavaScript用于复杂的功能和交互设计。
  - **jQuery**：简化HTML文档的遍历、事件处理、动画和Ajax交互。
  - **Thymeleaf**：服务器端的Java模板引擎，用于渲染Web视图，支持动态内容的生成。
- **数据库：**
  - **MyBatis**：数据库持久层框架，提供数据库操作的映射和执行。支持复杂查询、数据过滤及转换。
- **网络请求处理：**
  - **Ajax**：用于在后台异步请求数据，改善用户体验，减少页面重载。
- **部署：**
  - **Docker**：容器化解决方案，用于封装应用及其依赖，简化部署和测试，确保不同环境下应用的一致性。

### 二、项目设计（核心）

#### 1、数据处理（后端）：

> 在SpringBoot项目中，`model`、`dao`、`service`、和`controller`层各自承担不同职责且相互协作以构建高效的应用架构。`Model`层定义数据结构并在应用层间传递数据；`DAO`层负责数据持久化和数据库交互，使上层服务与数据访问逻辑解耦；`Service`层处理业务逻辑，调用DAO层实现数据操作；`Controller`层处理外部请求，调用Service层的业务逻辑，并向用户提供响应。这种分层确保了应用的高内聚低耦合性，提高了代码的可维护性和可扩展性。
>
> **关系**：
>
> - **数据流向**：用户的请求首先到达Controller层，Controller层解析请求后，调用Service层处理业务逻辑。Service层再调用DAO层与数据库交互，获取或更新所需数据。数据通过Model层的对象在各层之间传递。
> - **依赖关系**：Controller依赖Service层来执行业务逻辑；Service层依赖DAO层来访问数据库；DAO层可能依赖Model层来定义数据库与应用数据之间的映射。

##### 1）Model（模型）

> **定义**：Model层主要定义了应用的数据结构。通常包含数据的实体类，这些类通过成员变量和方法来描述数据的属性和行为。
>
> **作用**：Model层的类被用来在应用的不同层（如DAO、service和controller）之间传递数据。它们通常映射到数据库表结构，但也可以用来封装任何形式的数据。

###### **a. 用户模型（user）**

> 用户表模型主要用于存储和管理用户的基本信息和配置数据，这对于实现系统的用户管理、认证和个性化设置是至关重要的。它包含了诸如用户名、密码、昵称等基本身份信息，以及用户的存储空间配置和使用情况，如总存储空间和已使用空间。

|    属性名称     |  类型   |                    描述                    |
| :-------------: | :-----: | :----------------------------------------: |
|      `id`       | Integer | 用户的唯一标识符。通常在数据库中自动生成。 |
|   `username`    | String  |     用户的用户名，用于登录和用户识别。     |
|   `password`    | String  |    用户的密码，通常应该存储为加密形式。    |
|     `salt`      | String  |  加密密码时使用的盐值，增加密码的安全性。  |
|   `nickname`    | String  |       用户的昵称，用于显示在界面上。       |
|     `space`     |  float  |    用户的总存储空间，单位可能为GB或MB。    |
|   `usespace`    |  float  |           用户已使用的存储空间。           |
|  `filecounts`   |   int   |            用户存储的文件数量。            |
|    `avatar`     | String  |            用户的头像图片路径。            |
| `newavatarname` | String  |           用户更新的头像文件名。           |
|    `regdate`    |  Date   |           用户注册的日期和时间。           |

###### **b. 文件模型（myfile）**

> `Myfile` 类是一个文件模型，用于表示系统中存储的文件。它包含文件的基本信息和元数据，如文件名、大小、类型、存储路径以及上传和修改时间等。该模型还包括一些特定的状态标志，如是否为图片、是否在回收站中以及下载计数等，这些信息用于管理文件在云存储系统中的生命周期和访问控制。

|                        属性名                        |  类型   |                             描述                             |
| :--------------------------------------------------: | :-----: | :----------------------------------------------------------: |
|                         `id`                         | Integer |           文件的唯一标识符，通常由数据库自动分配。           |
|                    `oldfilename`                     | String  |                    用户上传的原始文件名。                    |
|                    `newfilename`                     | String  |    为存储目的可能更改的新文件名，用于处理重复或命名约定。    |
|                        `ext`                         | String  |  文件扩展名，从原始文件名中提取，以便根据文件类型进行处理。  |
|                        `path`                        | String  | 文件在服务器或存储架构中的存储路径，指示文件的物理存储位置。 |
|                        `size`                        |  float  |           文件的大小，通常以 MB 或 GB 的格式存储。           |
|                        `type`                        | String  |    文件的分类或类型指定，可能来自 MIME 类型或类似的分类。    |
|                       `isimg`                        |   int   | 标志指示文件是否为图像，这可能会影响文件的处理或在 UI 中的显示。 |
|                     `downcounts`                     |   int   |      跟踪文件被下载的次数的计数器，用于分析或限制访问。      |
|                     `uploadtime`                     |  Date   |                  记录文件最初上传的时间戳。                  |
|                     `changetime`                     |  Date   |                  记录文件最后修改的时间戳。                  |
|                     `isrecycle`                      |   int   |  状态标志，指示文件是否在回收站中，便于恢复或永久删除操作。  |
|                      `user_id`                       | Integer |  引用拥有或上传文件的用户，将其链接到用户特定的数据和权限。  |
| *独立于数据库的其他属性（帮助用户更好进行文件管理）* |         |                                                              |

| 属性名  |  类型  |                      描述                       |
| :-----: | :----: | :---------------------------------------------: |
|  icon   | String | 表示文件类型的图标的 URL 或路径，增强 UI 表示。 |
|   ids   |  int   |          用于 UI 显示和管理的内部 ID。          |
|  sort   | String |  指示排序方式的字符串，如按日期、名称等排序。   |
| sortway | String |      指示排序方向的字符串，如升序或降序。       |

**注**：*两个模型均重写**toString()**方法以提供类的属性值的字符串表示形式，调试时可以快速查看对象的状态。*

##### 2）DAO （数据访问对象）

> 用MyBatis框架进行数据库操作时，`Mapper`接口和其对应的XML文件之间的关系是核心的组成部分
>
> - Mapper接口是一个Java接口，它定义了访问数据库所需要的方法。
> - Mapper XML文件是MyBatis特有的配置文件，其中包含了具体的SQL语句和指令。
>
> MyBatis框架会自动将Mapper接口的每个方法与相应的XML文件中的SQL语句通过`id`（方法名）关联起来使得数据库操作与业务逻辑分离，同时提高了代码的可维护性和可扩展性。

###### **a. UserMapper**

> - Mapper接口
>
>   定义了多个方法以处理与用户数据相关的操作，如注册新用户、检查用户名是否存在、用户登录、获取用户信息以及更新用户头像和个人资料等。这些方法使得系统能够在后端有效地管理用户账户信息，支持身份验证，个人信息的查询与更新，以及用户文件数量的统计等功能。
>
> - Mapper XML
>
>   对Mapper接口定义的方法进行实现，对数据库进行实际访问和操作。

**核心功能（部分）**

```java
// 查询空间和头像
    public User findIndexInfo(Integer id);
// 修改个人信息
    public Integer setPersonInfo(User user);
```

```xml
<!--获取头像和空间-->
<select id="findIndexInfo" parameterType="Integer" resultType="com.nyist.model.User">
    select sum(myfile.size) as usespace,space,avatar,newavatarname,nickname from user,myfile where user.id = #{id}
    and user.id = myfile.user_id
</select>
<!--修改个人信息-->
<update id="setPersonInfo" parameterType="com.nyist.model.User">
    update user
    <set>
        <if test="nickname!=null and nickname!=''">
            nickname = #{nickname},
        </if>
        <if test="password!=null and password!=''">
            password = #{password},
        </if>
        <if test="salt!=null and salt!=''">
            salt = #{salt},
        </if>
    </set>
    where id = #{id}
</update>
```

###### **b. MyfileMapper**

> - Mapper接口
>
>   定义了一系列文件操作方法，涵盖文件上传、检索、下载计数更新、回收和彻底删除等功能。此接口旨在实现对用户文件的全面管理，包括通过用户ID获取文件存储使用情况、支持关键词搜索、以及处理文件的回收站功能，为系统提供基础的文件处理和查询服务。
>
> - Mapper XML
>
>   对Mapper接口定义的方法进行实现，对数据库进行实际访问和操作。

**核心功能（部分）**

```java
// 判断myfile表是否有数据
    public int findIdCounts(Integer user_id);
//关键字模糊查询
    public List<Myfile> findFilesByKey(Myfile myfile);
```

```xml
<!--判断myfile表是否有数据-->
<select id="findIdCounts" parameterType="Integer" resultType="int">
    select count(id) from myfile where user_id = #{user_id}
</select>
<!--根据条件查找文件-->
<select id="findFilesByKey" parameterType="com.nyist.model.Myfile" resultType="com.nyist.model.Myfile">
    select * from myfile where user_id = #{user_id} and isrecycle != 1
    <if test="oldfilename != null and oldfilename != ''">
        and oldfilename  like concat('%',#{oldfilename},'%')
    </if>
    <if test="type != null and type != ''">
        and type like concat('%',#{type},'%')
    </if>
    order by ${sort} ${sortway}
</select>
```

##### 3）Service（服务）

> **定义**：Service层位于DAO和Controller之间，主要负责业务逻辑的处理。
>
> **作用**：Service层使用DAO层提供的方法来访问数据库，并在这些数据上执行业务逻辑。它为Controller提供了一种方式来执行业务逻辑，同时保持业务逻辑与用户界面的分离。
>
> 服务层通常由接口（interface）和实现（impl）两部分构成。接口层定义了一系列与业务逻辑相关的方法，如用户管理或数据处理等，提供了一个清晰的协议供其他层如控制器层调用，而不涉及具体实现细节。实现层（impl）则负责具体执行这些定义好的方法，处理从数据库调用到第三方服务集成等业务逻辑的所有细节。

###### a. UserService

**核心功能（部分）**

**interface：**

```java
// 查询空间和头像
    public User findIndexInfo(Integer id);
// 修改个人信息
    public Integer setPersonInfo(User user);
```

**impl：**

```java
// 获取头像和空间
@Override
public User findIndexInfo(Integer id) {
	return this.userMapper.findIndexInfo(id);
}
// 修改个人信息
@Override
public Integer setPersonInfo(User user) {
    if (!("".equals(user.getPassword()))){
        //获取服务端自动生成的盐
        String salt = md5Utils.getRandomSalt();
        //设置密码
        user.setPassword(md5Utils.toMD5(user.getPassword()+salt));
        //更新盐
        user.setSalt(salt);
    }
    return this.userMapper.setPersonInfo(user);
}
```

###### b. MyfileService

**核心功能（部分）**
**interface：**

```java
// 判断myfile表是否有数据
    public int findIdCounts(Integer user_id);
//关键字模糊查询
    public List<Myfile> findFilesByKey(Myfile myfile);
```

**impl：**

```java
// 判断myfile表是否有数据
@Override
public int findIdCounts(Integer user_id) {
	return this.myfileMapper.findIdCounts(user_id);
}
// 根据条件查找文件
@Override
public List<Myfile> findFilesByKey(Myfile myfile) {
    List<Myfile> allFiles = this.myfileMapper.findFilesByKey(myfile);
    return this.fileTypeUtils.IconFilesList(allFiles);
}
```

##### 4）Controller（控制器）

> **定义**：Controller层主要处理外部请求，并将用户的请求路由到相应的服务逻辑。
>
> **作用**：Controller层调用Service层的方法来处理具体的业务逻辑，并将结果返回给用户。这一层通常负责处理HTTP请求，解析请求参数，并构建响应。

###### a. UserController

> `UserController`类位于SpringBoot项目的控制层，负责处理与用户相关的所有请求。该类通过注入的`UserService`和其他服务，如邮件任务`MailTask`和加密工具`Md5Utils`，提供了包括验证码获取、注册登录、账号密码登录、用户信息展示、头像上传、用户信息修改和登出等功能。

**核心功能（部分）**

主页展示

> `findIndexInfo`方法用于获取并返回当前登录用户的主页信息，包括头像、存储空间使用详情和昵称。方法首先通过`session`获取用户ID，并使用此ID调用`userService`以获取用户及其文件统计信息。然后，方法计算和格式化用户的存储空间使用情况：
>
> 1. **用户文件统计**：首先检查用户的文件数量（`filecounts`），这决定了是否有使用空间的数据可供计算。
> 2. **空间使用和总空间**：计算用户已使用的空间（`usespace`）和总空间（`space`），这些信息被转换为合适的单位（KB、MB、GB）并保留一定的小数位数。
> 3. **头像和昵称**：根据是否有新头像名称来决定使用默认头像还是用户设定的头像，并将昵称信息加入列表。
> 4. **空间使用百分比**：计算已使用空间占总空间的百分比。

```java
@GetMapping("/findindexinfo")
@ResponseBody
public List<String> findIndexInfo(HttpSession session){
    int filecounts = this.userService.findUserFileCounts((Integer) session.getAttribute("USER_ID"));
    User user = this.userService.findIndexInfo((Integer) session.getAttribute("USER_ID"));
    List<String> list = new LinkedList<>();
    //获取使用空间
    float usespace = 0;
    if (filecounts!=0){
        usespace = user.getUsespace();
    }
    //获取总空间
    float space = user.getSpace();
    //获取头像地址
    String avatar = user.getAvatar();
    //获取头像名称
    String newavatarname = user.getNewavatarname();
    //获取昵称
    String nickname = user.getNickname();
    //得到百分比
    String sp = (usespace/(space*1024*1024))*100+"%";

    //设置头像
    if ("0".equals(newavatarname)){
        list.add("/img/tou.jpg");
    }else{
        list.add(avatar+newavatarname);
    }
    //计算结果保留两位小数
    if (usespace<1024){
        list.add(usespace+"KB");
    }else if (usespace>=1024&&usespace<1048576){
        BigDecimal b= new BigDecimal(usespace/1024);
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        list.add(f1+"MB");
    }else{
        BigDecimal b= new BigDecimal(usespace/1024/1024);
        float f2 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        list.add(f2+"GB");
    }
    list.add(String.valueOf((long)space));
    list.add(nickname);
    list.add(sp);
    return list;
}
```

账号信息

> `findInfoById`方法主要用于获取用户的账号信息并展示在个人中心页面。它通过HTTP会话（`HttpSession`）获取当前登录用户的ID，然后调用`userService`的`findUserFileCounts`和`findInfoById`方法来分别获取用户的文件数量和详细信息。
>
> 方法流程如下：
>
> 1. **获取文件数量**：通过用户ID查询用户拥有的文件数量，这有助于计算用户的使用空间。
> 2. **获取用户信息**：通过`findInfoById`方法获取用户的详细信息，如用户名、空间使用情况等。
> 3. **计算空间使用**：如果用户有文件（文件计数非零），方法会计算使用的空间（`usespace`）。使用空间会根据大小转换为适当的单位（KB、MB、GB），并使用`BigDecimal`进行精确计算以避免浮点运算误差。
> 4. **更新模型**：将计算后的空间使用情况和用户信息添加到`Model`中，这样它们就可以在视图（`subpage/person`）中被访问和显示。
> 5. **返回视图**：最后，方法返回一个视图名称字符串，指向一个子页面（`subpage/person`），该页面用于显示个人信息。

```java
@GetMapping("/personinfo")
public String findInfoById(HttpSession session,Model model){
    int filecounts = this.userService.findUserFileCounts((Integer) session.getAttribute("USER_ID"));
    User userinfo = this.userService.findInfoById((Integer) session.getAttribute("USER_ID"));
    float usespace = 0;
    if (filecounts!=0){
        usespace = userinfo.getUsespace();
    }

    //计算结果保留两位小数
    if (usespace<1024){
        model.addAttribute("usespace",usespace+"KB");
    }else if (usespace>=1024&&usespace<1048576){
        BigDecimal b= new BigDecimal(usespace/1024);
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        model.addAttribute("usespace",f1+"MB");
    }else{
        BigDecimal b= new BigDecimal(usespace/1024/1024);
        float f2 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        model.addAttribute("usespace",f2+"GB");
    }
    model.addAttribute("userInfo",userinfo);
    return "subpage/person";
}
```

###### b. MyfileController

> `MyfileController`类在Spring MVC框架中负责处理与文件操作相关的请求，包括文件的上传、下载、查看、回收和恢复。它通过依赖注入`MyfileService`和`FileTypeUtils`，实现了业务逻辑的分离和管理，提供了丰富的文件管理功能如文件上传空间检查、文件分类展示、文件回收站管理以及基于关键词的文件搜索，使得用户能够有效地管理其文件存储和访问。

**核心功能（部分）**

主页展示

> `uploadFiles` 方法中负责处理文件上传请求，允许用户将多个文件上传到服务器。该方法执行以下关键步骤：
>
> 1. **用户验证**：首先从 HTTP 会话中获取用户 ID，确保用户已登录且会话有效。
> 2. **空间检查**：在上传文件之前，方法检查数据库中关于用户已使用的空间。这是为了确保用户没有超出其分配的存储限额。
> 3. **文件处理**：对于传入的每个文件，方法获取文件的原始名称、后缀名，并生成一个新的文件名，以确保系统中的唯一性。文件的大小和类型也被记录下来。
> 4. **文件存储**：设置一个本地存储路径，并检查路径是否存在，不存在则创建。对于每个文件，如果用户的剩余空间足够，文件将被保存到指定路径；如果空间不足，将停止上传过程并返回错误信息。
> 5. **数据库更新**：每上传成功一个文件，文件的信息（包括新的文件名、路径、大小、类型等）会被保存到数据库中，以便于管理和检索。

```java
@PostMapping("/uploadfiles")
@ResponseBody
public WebPageVo uploadFiles(@RequestParam("files") List<MultipartFile> files, HttpSession session) {
    //获取session中的邮箱
    Integer user_id = (Integer) session.getAttribute("USER_ID");
    //准备数据
    float useSpace = 0;
    //首先判断myfile表是否有数据（防止空指针异常）
    int idCounts = this.myfileService.findIdCounts(user_id);

    WebPageVo webPageVo = new WebPageVo();
    //循环遍历所有文件
    for (MultipartFile file : files) {
        //获取文件原始名称
        String oldfilename = file.getOriginalFilename();
        //获取文件后缀名
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        //生成新的文件名称
        String newfilename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +"_"+ UUID.randomUUID().toString().replace("-","") +"_"+ oldfilename;
        //设置文件上传路径
        String path = "D:\\yunfile\\files\\";
        //获取文件大小(单位kb)
        float size = (((float)file.getSize())/1024);
        //获取文件类型
        String type = file.getContentType();
        //创建文件对象目录
        File filepath = new File(path);
        if (!filepath.exists()) {
            filepath.mkdirs();
        }
        //上传文件
        try {
            if (idCounts>0){//大于零表示有数据，然后执行查询使用空间的sql
                useSpace = this.myfileService.findUseSpace(user_id);
            }//否则使用默认值
            if ((6291456-useSpace)>size){
                file.transferTo(new File(path+newfilename)); //核心上传方法
                webPageVo.setMsg(newfilename);
                webPageVo.setCode(0);
            }else{
                webPageVo.setMsg("空间不足");
                webPageVo.setCode(1);
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            webPageVo.setMsg("上传失败,请重新上传");
            webPageVo.setCode(1);
        }
        //将文件信息保存数据库
        Myfile myfile = new Myfile();
        myfile.setOldfilename(oldfilename);myfile.setNewfilename(newfilename);
        myfile.setExt(ext);myfile.setPath("/yun/file/");myfile.setSize(size);
        myfile.setType(type);myfile.setUser_id(user_id);
        try {
            this.myfileService.uploadFiles(myfile);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    return webPageVo;
}
```

账号信息

> `findFilesByKey` 方法实现了条件模糊查询文件的功能，根据用户输入的关键字、排序方式等条件查找符合条件的文件，并将结果显示在页面上。
>
> 1. **参数处理**：
>    - **`keys`**：接收用户输入的搜索关键字，若未输入则默认为空字符串。
>    - **`sort`**：指定排序字段，默认为文件原始名称（`oldfilename`）。
>    - **`sortway`**：排序方式，默认为升序（`asc`）。
> 2. **会话管理**：
>    - 使用`session`获取当前登录用户的ID (`user_id`)。
>    - 将搜索关键字和排序字段保存在`session`中，便于用户再次访问时记住选择。
> 3. **关键字解析**：
>    - 使用`fileTypeUtils.ParsingKeys`方法解析输入的关键字，将其分解成类型和名称部分，用于构建查询条件。
> 4. **数据库查询**：
>    - 根据解析后的关键字、用户ID、排序字段和方式构建`Myfile`对象。
>    - 调用`myfileService.findFilesByKey(myfile)`方法执行数据库查询，返回匹配的文件列表。
> 5. **页面数据设置**：
>    - 将查询到的文件列表、排序方式和提示信息添加到模型（`model`）中，供前端页面显示。
>    - `sorttip`由`fileTypeUtils.setSortTip`方法生成，提供用户友好的排序提示。
> 6. **视图渲染**：
>    - 返回"subpage/file"页面，使用模型中的数据显示文件列表和相关信息。

```java
@GetMapping("/findfilesbykey")
public String findFilesByKey(String keys,String sort,
                             @RequestParam(required = false,defaultValue = "asc")String sortway,
                             HttpSession session,Model model){
    //保存前一次搜索关键字
    if (keys==null) {
        if (session.getAttribute("keys")==null) {
            session.setAttribute("keys","");
        }
    } else {
        session.setAttribute("keys",keys);
    }
    if (sort==null) {
        if (session.getAttribute("sort")==null) {
            session.setAttribute("sort","oldfilename");
        }
    } else {
        session.setAttribute("sort",sort);
    }

    //获取session
    Integer user_id = (Integer) session.getAttribute("USER_ID");

    //分析关键字
    List<String> keylist = this.fileTypeUtils.ParsingKeys((String) session.getAttribute("keys"));
    //创建文件对象并设置参数
    Myfile myfile = new Myfile();
    myfile.setType(keylist.get(0));
    myfile.setOldfilename(keylist.get(1));
    myfile.setUser_id(user_id);
    myfile.setSort((String) session.getAttribute("sort"));
    myfile.setSortway(sortway);

    //执行操作
    List<Myfile> files = this.myfileService.findFilesByKey(myfile);

    String sorttip = this.fileTypeUtils.setSortTip((String) session.getAttribute("sort"));

    model.addAttribute("files",files);
    model.addAttribute("sort",session.getAttribute("sort"));
    model.addAttribute("sortway",sortway);
    model.addAttribute("sorttip",sorttip);
    return "subpage/file";
}
```

##### 5）Utils

###### a. MailTask

> MailTask 类是一个工具类，负责发送验证码邮件。通过 @Autowired 注入 JavaMailSender 接口，getMailCode 方法接收邮箱地址，生成 6 位随机验证码。创建 SimpleMailMessage 对象，设置邮件主题、正文、收件人和发件人后，使用 mailSender.send(message) 方法发送邮件，最后返回生成的验证码字符串。

###### b. FileTask

> FileTypeUtils 是一个工具类，提供多种文件类型处理和解析方法。IconFilesList 方法接受一个文件列表，根据文件扩展名和类型设置每个文件的图标标识。ParsingKeys 方法解析搜索关键字，将其转换为对应的文件类型和名称，用于模糊查询。setSortTip 方法根据传入的排序字段返回相应的排序提示文本，帮助用户理解当前的排序方式。该类通过这些方法简化和标准化了文件类型和关键字处理的逻辑。

#### 2、信息展示（前端）

> 本项目的前端采用了基础的HTML、CSS、JavaScript和jQuery，结合了Thymeleaf模板引擎，以实现动态页面渲染和交互功能。前端主要包含以下几个模块：
>
> 1. **用户登录与注册**：
>    - 实现了通过邮箱验证码和账号密码的两种登录方式。
>    - 用户注册和登录页面通过Thymeleaf渲染，包含表单验证和提交功能。
> 2. **个人中心**：
>    - 展示用户的基本信息，包括用户名、昵称、注册日期等。
>    - 通过AJAX请求获取并显示用户的存储使用情况和上传的文件总数。
>    - 允许用户修改头像、昵称和密码。
> 3. **文件管理**：
>    - 文件上传功能支持多文件上传，通过AJAX将文件传输到服务器。
>    - 文件列表展示用户上传的所有文件，提供文件下载、查看详细信息、批量操作等功能。
>    - 前端使用了CSS和jQuery进行文件列表的样式处理和交互操作。
> 4. **相册管理**：
>    - 图片文件以相册形式展示，提供文件下载、查看详细信息等功能。
>    - 使用jQuery实现图片加载和展示效果。
> 5. **回收站**：
>    - 展示用户删除的文件，允许用户恢复或永久删除文件。
>    - 通过AJAX请求实现文件的恢复和删除操作。
> 6. **整体设计**：
>    - 页面布局和样式通过CSS和jQuery实现，确保页面在不同设备上的一致性。
>    - 动态内容的加载和交互操作使用AJAX技术，提升用户体验。
>    - 结合Thymeleaf模板引擎，实现了前后端数据的无缝传递和页面渲染。

Layui框架中的`upload`、`element`和`layer`三个模块来实现文件上传功能。

- 普通图片上传

  > 通过upload.render方法进行配置，绑定了一个上传按钮（elem），指定了上传的接口URL（url），并设置了上传字段名（field）和HTTP请求方法（method）。在上传前（before回调）预览本地图片，并初始化进度条。在上传成功（done回调）和失败（error回调）时分别进行相应的处理，成功时清空错误状态，失败时显示重试按钮，并提供进度条显示（progress回调），在上传完毕时显示提示信息。
  >
  > ```js
  > var uploadInst = upload.render({
  > elem: '#test1'
  > ,url: '/user/uploadavatar' //此处用的是第三方的 http 请求演示，实际使用时改成您自己的上传接口即可。
  > ,field: 'avatar'
  > ,method: 'post'
  > ,before: function(obj){
  >   //预读本地文件示例，不支持ie8
  >   obj.preview(function(index, file, result){
  >       $('#demo1').attr('src', result); //图片链接（base64）
  >   });
  > 
  >   element.progress('demo', '0%'); //进度条复位
  >   layer.msg('上传中', {icon: 16, time: 0});
  > }
  > ,done: function(res){
  >   //如果上传失败
  >   if(res.code > 0){
  >       return layer.msg('上传失败');
  >   }
  >   //上传成功的一些操作
  >   //……
  >   $('#demoText').html(''); //置空上传失败的状态
  > }
  > ,error: function(){
  >   //演示失败状态，并实现重传
  >   var demoText = $('#demoText');
  >   demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
  >   demoText.find('.demo-reload').on('click', function(){
  >       uploadInst.upload();
  >   });
  > }
  > 
  > //进度条
  > ,progress: function(n, elem, e){
  >   element.progress('demo', n + '%'); //可配合 layui 进度条元素使用
  >   if(n == 100){
  >       layer.msg('上传完毕', {icon: 1});
  >   }
  > }
  > });
  > ```

- 多文件上传列表

  > 通过`upload.render`方法进行配置，绑定上传按钮（`elem`），指定上传接口URL（`url`），设置上传字段名（`field`）、HTTP请求方法（`method`）、允许多文件上传（`multiple`）和最大上传文件数（`number`）。在`choose`回调中，将选中的文件添加到文件队列，并预览每个文件，生成相应的表格行，提供单个文件的重传和删除功能。在`done`回调中，处理上传成功的文件，清空已上传文件的操作。在`error`回调中，显示上传失败的信息并提供重传按钮。在`progress`回调中，显示文件上传进度条。`allDone`回调在所有文件上传完成后执行，记录上传状态。
  >
  > ```js
  > var uploadListIns = upload.render({
  >  elem: '#testList'
  >  ,elemList: $('#demoList') //列表元素对象
  >  ,url: '/files/uploadfiles' //此处用的是第三方的 http 请求演示，实际使用时改成您自己的上传接口即可。
  >  ,field: 'files'
  >  ,accept: 'file'
  >  ,method: 'post'
  >  ,multiple: true
  >  ,number: 6
  >  ,auto: false
  >  ,bindAction: '#testListAction'
  >  ,choose: function(obj){
  >      var that = this;
  >      var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
  >      //读取本地文件
  >      obj.preview(function(index, file, result){
  >          var tr = $(['<tr id="upload-'+ index +'">'
  >              ,'<td>'+ file.name +'</td>'
  >              ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
  >              ,'<td><div class="layui-progress" lay-filter="progress-demo-'+ index +'"><div class="layui-progress-bar" lay-percent=""></div></div></td>'
  >              ,'<td>'
  >              ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
  >              ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
  >              ,'</td>'
  >              ,'</tr>'].join(''));
  > 
  >          //单个重传
  >          tr.find('.demo-reload').on('click', function(){
  >              obj.upload(index, file);
  >          });
  > 
  >          //删除
  >          tr.find('.demo-delete').on('click', function(){
  >              delete files[index]; //删除对应的文件
  >              tr.remove();
  >              uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
  >          });
  > 
  >          that.elemList.append(tr);
  >          element.render('progress'); //渲染新加的进度条组件
  >      });
  >  }
  >  ,done: function(res, index, upload){ //成功的回调
  >      var that = this;
  >      if(res.code === 0){ //上传成功
  >          var tr = that.elemList.find('tr#upload-'+ index)
  >              ,tds = tr.children();
  >          tds.eq(3).html('success'); //清空操作
  >          delete this.files[index]; //删除文件队列已经上传成功的文件
  >          return;
  >      }
  >      this.error(index, upload);
  >  }
  >  ,allDone: function(obj){ //多文件上传完毕后的状态回调
  >      console.log(obj)
  >  }
  >  ,error: function(index, upload){ //错误回调
  >      var that = this;
  >      var tr = that.elemList.find('tr#upload-'+ index)
  >          ,tds = tr.children();
  >      tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
  >      layer.msg("请检查空间是否已满")
  >  }
  >  ,progress: function(n, elem, e, index){ //注意：index 参数为 layui 2.6.6 新增
  >      element.progress('progress-demo-'+ index, n + '%'); //执行进度条。n 即为返回的进度百分比
  >  }
  > });
  > ```
  >

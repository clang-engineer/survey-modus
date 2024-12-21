# Expert Form Maker (exformmaker) 개요

- 전문가가 고객사별로 수집이 필요한 정보 수집 폼을 만들고, 고객사별로 수집된 정보를 관리할 수 있는 시스템

# 기술 개요

- 백엔드는 kotiln, 프론트엔드는 react로 구성한다. >> spa구조화 되어있는 khipster로 구성한다.
- 메타 정보는 postgresql에 동적으로 생기는 폼에서 수집하는 정보는 mongodb를 사용한다.
- 폼별로 콜렉션을 생성하고 건건을 document로 저장한다.

---

# 사용자 스토리

## 폼관리

1. 폼을 생성한다. (사전에 등록되어 있는 카테고리 반드시 지정 >> 카테고리가 향후 mongodb에 콜렉션으로 구분된다.)

- 카테고리 선택시 추천폼 목록이 나타난다.
- 추천폼을 선택하면 해당 폼의 필드가 자동으로 추가된다. 이후에도 필드를 커스터마이징할 수 있다.

2. 폼에 필드를 추가한다. (필드 속성들을 입력)

## 회사 관리

1. 회사를 생성한다.
2. 회사가 사용할 수 있는 폼을 설정(할당)한다.
3. 회사에 속한 스태프(직원) 정보를 입력한다.

- 위 직원들이 폼을 확인하고 데이터를 입력하는 주체
- 직원은 회사 고유 링크를 통해 접속하고 sms인증을 통해 회사 폼에 접근할 수 있다.
- 직원이 접속시 회사 폼 목록이 나타난다.
- 직원이 폼을 선택하면 해당 폼의 데이터를 조회 및 수정할 수 있다.

## 그룹 관리

1. 그룹을 생성한다.

- 그룹을 통해 본인이 관리하는 회사들을 다른 사용자에게 공유할 수 있다.

2. 해당 그룹에 소속되는 회사와 사용자를 설정한다. (\* 사용자 == 전문가 == 시스템 로그인)

- 회사는 여러 그룹에 속할 수 있다.
- 사용자는 여러 그룹에 속할 수 있다.
- 사용자를 그룹에 설정하면 해당 사용자에게 초대 메일이 발송된다.
- 사용자가 초대 메일을 수신하면 해당 그룹이 소속된 회사의 폼 목록에 접근할 수 있다.
- 최초 접속시 속한 그룹별 회사 목록이 나타난다.

3. 소속 그룹의 회사 화면에 접근할 수 있다.

- 그룹에 시스템에 가입된 사용자를 초대할 수 있다.
- 그룹에 초대된 사용자는 해당 그룹의 회사 정보(폼 목록)에 접근할 수 있다.

### 직원 관리 (회사에 속한 직원)

- 회사에 속한 직원을 만들 수 있다.
- 직원은 2차 인증 후에 회사의 폼 목록에 접근할 수 있고, 각 폼에 데이터를 조회 및 수정할 수 있다.
  (직원이 주체적으로 가입하는 형태가 아니라, 전문가가 직원을 생성하고 초대하는 형태로 진행한다.)

---

# 작업 진척률

## erd 설계 &nbsp;&nbsp; ![100%](https://progress-bar.xyz/100)

## 메타 및 관리자 ui 개발 &nbsp;&nbsp; ![in progress](https://progress-bar.xyz/30/?title=in%20progress)

- [x] 회사관리(Company) &nbsp;&nbsp; ![100%](https://progress-bar.xyz/100)
- [x] 그룹관리(Group) &nbsp;&nbsp; ![100%](https://progress-bar.xyz100)
- [x] 그룹 회사관리 &nbsp;&nbsp; ![100%](https://progress-bar.xyz/100) >> @ManyToMany로 처리
- [x] 그룹 사용자관리(GroupUser) &nbsp;&nbsp; ![100%](https://progress-bar.xyz/100) >> @ManyToMany로 처리
- [x] 카테고리 관리 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0)
- [x] 폼관리 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0)
- [x] 회사 폼관리 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0) >> @ManyToMany로 처리
- [ ] 스태프 관리 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0) >> @Embedded로 처리
- [ ] 필드 관리 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0) >> @ManyToOne로 처리
- [ ] mongo db 연동 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0)

- [ ] 실사용자 접근 api 및 ui 개발 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0)
- [ ] 스태프 접근 api 및 ui 개발 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0)

---

# ERD

<iframe width="600" height="336" src="https://www.erdcloud.com/p/DwEHf4YkCGfA3y3Cq" frameborder="0" allowfullscreen></iframe>

- [erdcloud](https://www.erdcloud.com/p/DwEHf4YkCGfA3y3Cq)

```html
<iframe width="600" height="336" src="https://www.erdcloud.com/p/DwEHf4YkCGfA3y3Cq" frameborder="0" allowfullscreen></iframe>
```

1. form 만들기 >> 필수
2. 회사 만들기 (폼 할당, 직원 추가) >> 필수
3. 그룹 만들기 (회사 할당, 사용자 추가) >> 필수 아님

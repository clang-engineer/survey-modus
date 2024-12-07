# Expert Form Maker (exformmaker) 개요

- 전문가가 고객사별로 수집이 필요한 정보 수집 폼을 만들고, 고객사별로 수집된 정보를 관리할 수 있는 시스템

# 기술 개요

- 백엔드는 kotiln, 프론트엔드는 react로 구성한다. >> spa구조화 되어있는 khipster로 구성한다.
- 메타 정보는 postgresql에 동적으로 생기는 폼에서 수집하는 정보는 mongodb를 사용한다.
- 폼별로 콜렉션을 생성하고 건건을 document로 저장한다.

---

# 사용자 스토리

## 폼관리

1. 폼을 생성한다.
2. 폼에 필드를 추가한다.

## 회사 관리

1. 회사를 생성한다.
2. 회사가 사용할 수 있는 폼을 설정(할당)한다.
3. 회사에 속한 직원을 생성한다.

- 직원은 회사 고유 링크를 통해 sms인증을 통해 회사 폼에 접근할 수 있다.

## 그룹 관리

1. 관리를 원하는 그룹을 생성한다.
2. 해당 그룹에 소속되는 회사와 사용자를 설정한다. (\* 사용자 == 전문가 == 시스템 로그인)

- 회사는 여러 그룹에 속할 수 있다.
- 사용자는 여러 그룹에 속할 수 있다.
- 사용자를 그룹에 설정하면 해당 사용자에게 초대 메일이 발송된다.

3. 소속 그룹의 회사 화면에 접근할 수 있다.

- 그룹에 시스템에 가입된 사용자를 초대할 수 있다.
- 그룹에 초대된 사용자는 해당 그룹의 회사 정보(폼 목록)에 접근할 수 있다.

### 회사 관리

- 관리하는 회사를 만들 수 있다.
- 관리하는 폼을 만들 수 있다.
- 회사가 사용할 수 있는 폼을 설정할 수 있다.

### 직원 관리 (회사에 속한 직원)

- 회사에 속한 직원을 만들 수 있다.
- 직원은 2차 인증 후에 회사의 폼 목록에 접근할 수 있고, 각 폼에 데이터를 조회 및 수정할 수 있다.
  (직원이 주체적으로 가입하는 형태가 아니라, 전문가가 직원을 생성하고 초대하는 형태로 진행한다.)

---

# 작업 진척률

## erd 설계 &nbsp;&nbsp; ![100%](https://progress-bar.xyz/100)

## 메타 및 관리자 ui 개발 &nbsp;&nbsp; ![in progress](https://progress-bar.xyz/30/?title=in%20progress)

- 그룹관리 &nbsp;&nbsp; ![100%](https://progress-bar.xyz/100)
- 사용자 그룹관리 &nbsp;&nbsp; ![100%](https://progress-bar.xyz/100)
- 회사관리 &nbsp;&nbsp; ![100%](https://progress-bar.xyz/100)
- 사용자 회사관리 &nbsp;&nbsp; ![100%](https://progress-bar.xyz/100)
- 폼관리 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0)
- 사용자 폼관리 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0)
- 스태프 관리 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0)
- 필드 관리 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0)
- mongo db 연동 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0)

- 실사용자 접근 api 및 ui 개발 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0)
- 스태프 접근 api 및 ui 개발 &nbsp;&nbsp; ![0%](https://progress-bar.xyz/0)

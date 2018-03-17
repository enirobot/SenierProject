# install.packages("Rserve") // spring과 R 연동을 위한 라이브러리
# library(Rserve)
# Rserve(args="--no-save")
# Rserve(FALSE,port=6311,args='--RS-encoding utf8 --no-save --slave --encoding utf8')

library("rJava")
library("KoNLP")
library("wordcloud")
library("RColorBrewer")
#useSejongDic()  # 사전등록
useNIADic()      # sejonDic보다 단어가 더 많음

par(family="AppleGothic") # 한글 글씨체 설정


# buildDictionary(ext_dic = "woorimalsam", user_dic = data.frame("가상화폐", "ncn"), replace_usr_dic = T)
# buildDictionary(ext_dic = "woorimalsam", user_dic = data.frame("우리카드", "ncn"), replace_usr_dic = T)
# buildDictionary(ext_dic = "woorimalsam", user_dic = data.frame("방탄소년단", "ncn"), replace_usr_dic = T)
# buildDictionary(ext_dic = "woorimalsam", user_dic = data.frame("박지수", "ncn"), replace_usr_dic = T)
# buildDictionary(ext_dic = "woorimalsam", user_dic = data.frame("삼성전자", "ncn"), replace_usr_dic = T)
# buildDictionary(ext_dic = "woorimalsam", user_dic = data.frame("코스피", "ncn"), replace_usr_dic = T)
# buildDictionary(ext_dic = "woorimalsam", user_dic = data.frame("코스닥", "ncn"), replace_usr_dic = T)


DF <- read.csv('/Users/Enirobot/SenierProject/Web_scraping/news.csv',
               stringsAsFactors = FALSE)                             # stringsAsFactors = FALSE : 문자형으로 가져옴, True면 요인(factor)형으로 가져옴

data <- sapply( unique(DF["Title"]), # unique() : 중복된 행 삭제
                extractNoun,         # extractNoun : 명사 추출
                USE.NAMES = F )      # USE.NAMES = F : 원문장 제외

data_unlist <- unlist(data)                                          # 리스트 -> 벡터로 바꿈
data_unlist <- gsub("[^ㄱ-ㅣ가-힣]+", "", data_unlist)               # 한글 이외의 것 제외
data_unlist <- gsub("포토", "", data_unlist)
data_unlist <- gsub("사진", "", data_unlist)
data_unlist <- gsub("뉴스", "", data_unlist)
data_unlist <- gsub("오늘", "", data_unlist)
data_unlist <- gsub("최고", "", data_unlist)
data_unlist <- gsub("우리", "", data_unlist)
data_unlist <- gsub("오마이", "", data_unlist)

data_Filter <- Filter( function(x) {nchar(x) >= 2}, data_unlist)    # 2글자 이상만, nchar() : 문자열 길이 반환

wordcount <- table(data_Filter)                                     # 빈도수 출력을 위해 table로 바꿈

wordcount_top <-head(sort(wordcount, decreasing = T), 100)          # 최대 빈도 상위 100개

palete <- brewer.pal(8, "Dark2")                                    # 색상 설정    

wordcloud(names(wordcount_top),             # 단어
          freq = wordcount_top,             # 빈도수
          scale = c(4, 0.5),                # 단어의 크기
          rot.per = 0.1,                    # 90도 회전시킨 단어 비율
          min.freq = 1,                     # 최소 단어 빈도
          random.order = F,                 # 고빈도 단어 중앙에 배치
          colors = palete,                  # 색상 목록
          family="AppleGothic")             # family="AppleGothic" : 폰트 지정


# pie(wordcount_top)                        # 원형 그래프

# barplot(wordcount_top)

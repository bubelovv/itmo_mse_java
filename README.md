# Репозиторий с материалами по курсу Java

## Порядок сдачи заданий

1. Клонировать данный репозиторий и запушить в СВОЙ НОВЫЙ ПРИВАТНЫЙ репозиторий. Добавляем оригинальный репозиторий в качестве `upstream`
```
git clone git@github.com:DKARAGODIN/ITMO_MSE_JAVA.git
git remote remove origin
git remote add origin *URL TO YOUR NEW PRIVATE REPO*
git remote add upstream git@github.com:DKARAGODIN/ITMO_MSE_JAVA.git
```

2. Домашки выкладываются в ветки вида `task-N-title`, контрольные в ветки `control-N-title`

3. Для сдачи задания требуется добавить ветку из основного репозитория в свой и запушить в свой репозиторий

```
git fetch upstream
git checkout upstream/task-N-title
git checkout -b task-N-title
git push origin task-N-title
```

4. Создать ветку `task-N-title-dev`, в которую добавляется решение

```
git checkout -b task-N-title-dev
```

5. При готовности задания запушить `dev` ветку и создать Pull Request. В качестве reviewr назначить меня.
   Заголовок PR должен быть в следующем виде: **Фамилия Имя. Task N** или **Фамилия Имя. Control N**.

6. Требования к ПО: Java 21, Maven 3.9.9. 
